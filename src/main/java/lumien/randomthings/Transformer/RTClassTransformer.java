package lumien.randomthings.Transformer;

import java.util.Iterator;

import lumien.randomthings.Configuration.ConfigBlocks;
import lumien.randomthings.Configuration.VanillaChanges;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.*;

import net.minecraft.launchwrapper.IClassTransformer;

public class RTClassTransformer implements IClassTransformer
{
	Logger coreLogger = LogManager.getLogger("RandomThingsCore");

	private static final String OBF_WORLD = "afn";

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if (name.equals("amp") && VanillaChanges.FASTER_LEAVEDECAY)
		{
			return patchLeaveClass(basicClass, true);
		}
		else if (name.equals("net.minecraft.block.BlockLeavesBase") && VanillaChanges.FASTER_LEAVEDECAY) // ClassCircularityError
		{
			return patchLeaveClass(basicClass, false);
		}
		else if (name.equals("bll") && VanillaChanges.HARDCORE_DARKNESS)
		{
			return patchEntityRendererClass(basicClass, true);
		}
		else if (name.equals("net.minecraft.client.renderer.EntityRenderer") && VanillaChanges.HARDCORE_DARKNESS)
		{
			return patchEntityRendererClass(basicClass, false);
		}
		else if (name.equals(OBF_WORLD))
		{
			return patchWorldClass(basicClass, true);
		}
		else if (name.equals("net.minecraft.world.World"))
		{
			return patchWorldClass(basicClass, false);
		}

		return basicClass;
	}

	private byte[] patchWorldClass(byte[] basicClass, boolean obfuscated)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		coreLogger.log(Level.INFO, "Found World Class: " + classNode.name + ":" + obfuscated);

		String sunBrightnessName = obfuscated ? "b" : "getSunBrightness";
		String indirectlyPoweredName = obfuscated ? "v" : "isBlockIndirectlyGettingPowered";

		int removeIndex = 0;

		MethodNode getSunBrightness = null;
		MethodNode isBlockIndirectlyGettingPowered = null;

		for (MethodNode mn : classNode.methods)
		{
			if (mn.name.equals(sunBrightnessName) && mn.desc.equals("(F)F"))
			{
				getSunBrightness = mn;
			}
			else if (mn.name.equals(indirectlyPoweredName) && mn.desc.equals("(III)Z"))
			{
				isBlockIndirectlyGettingPowered = mn;
			}
		}

		if (getSunBrightness != null && VanillaChanges.HARDCORE_DARKNESS)
		{
			for (int i = 0; i < getSunBrightness.instructions.size(); i++)
			{
				AbstractInsnNode an = getSunBrightness.instructions.get(i);
				if (an instanceof LdcInsnNode)
				{
					LdcInsnNode lin = (LdcInsnNode) an;

					if (lin.cst.equals(new Float("0.8")))
					{
						removeIndex = i;
					}
				}
			}
			for (int i = 0; i < 4; i++)
			{
				getSunBrightness.instructions.remove(getSunBrightness.instructions.get(removeIndex));
			}

		}

		if (isBlockIndirectlyGettingPowered != null && ConfigBlocks.wirelessLever)
		{
			LabelNode l0 = new LabelNode(new Label());
			LabelNode l1 = new LabelNode(new Label());
			LabelNode l2 = new LabelNode(new Label());
			String world = obfuscated?OBF_WORLD:"net/minecraft/world/World";
			
			isBlockIndirectlyGettingPowered.instructions.insert(l2);
			isBlockIndirectlyGettingPowered.instructions.insert(new InsnNode(IRETURN));
			isBlockIndirectlyGettingPowered.instructions.insert(new InsnNode(ICONST_1));
			isBlockIndirectlyGettingPowered.instructions.insert(l1);
			isBlockIndirectlyGettingPowered.instructions.insert(new JumpInsnNode(IFEQ, l2));
			isBlockIndirectlyGettingPowered.instructions.insert(new MethodInsnNode(INVOKESTATIC, "lumien/randomthings/Handler/CoreHandler", "isIndirectlyGettingPowered", "(L"+world+";III)Z"));
			isBlockIndirectlyGettingPowered.instructions.insert(new VarInsnNode(ILOAD,3));
			isBlockIndirectlyGettingPowered.instructions.insert(new VarInsnNode(ILOAD,2));
			isBlockIndirectlyGettingPowered.instructions.insert(new VarInsnNode(ILOAD,1));
			isBlockIndirectlyGettingPowered.instructions.insert(new VarInsnNode(ALOAD,0));
			isBlockIndirectlyGettingPowered.instructions.insert(l0);
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);

		return writer.toByteArray();
	}

	private byte[] patchEntityRendererClass(byte[] basicClass, boolean obfuscated)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		coreLogger.log(Level.INFO, "Found EntityRenderer Class: " + classNode.name + ":" + obfuscated);

		String methodName = obfuscated ? "h" : "updateLightmap";

		int removeIndex = 0;

		MethodNode updateLightmap = null;

		for (MethodNode mn : classNode.methods)
		{
			if (mn.name.equals(methodName))
			{
				updateLightmap = mn;
			}
		}
		if (updateLightmap != null)
		{
			for (int i = 0; i < updateLightmap.instructions.size(); i++)
			{
				AbstractInsnNode an = updateLightmap.instructions.get(i);
				if (an instanceof VarInsnNode)
				{
					VarInsnNode iin = (VarInsnNode) an;
					if (iin.getOpcode() == ISTORE && iin.var == 21)
					{
						updateLightmap.instructions.insert(iin, new IincInsnNode(21, -14));
						updateLightmap.instructions.insert(iin, new IincInsnNode(20, -14));
						updateLightmap.instructions.insert(iin, new IincInsnNode(19, -14));
					}
				}
				else if (an instanceof LdcInsnNode)
				{
					LdcInsnNode lin = (LdcInsnNode) an;

					if (lin.cst.equals(new Float("0.95")))
					{
						removeIndex = i;
					}
				}
			}
			for (int i = 0; i < 4; i++)
			{
				updateLightmap.instructions.remove(updateLightmap.instructions.get(removeIndex));
			}

		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);

		return writer.toByteArray();
	}

	private byte[] patchLeaveClass(byte[] basicClass, boolean obfuscated)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		coreLogger.log(Level.INFO, "Found Leave Class: " + classNode.name + ":" + obfuscated);

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);

		String methodName = obfuscated ? "a" : "onNeighborBlockChange";
		String worldClass = obfuscated ? OBF_WORLD : "net/minecraft/world/World";
		String leaveClass = obfuscated ? "amp" : "net/minecraft/block/BlockLeavesBase";
		String blockClass = obfuscated ? "ahu" : "net/minecraft/block/Block";

		MethodVisitor mv = writer.visitMethod(ACC_PUBLIC, methodName, "(L" + worldClass + ";IIIL" + blockClass + ";)V", null, null);
		mv.visitCode();

		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(81, l0);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitVarInsn(ILOAD, 3);
		mv.visitVarInsn(ILOAD, 4);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESTATIC, "lumien/randomthings/Handler/CoreHandler", "handleLeaveDecay", "(L" + worldClass + ";IIIL" + blockClass + ";)V");
		Label l1 = new Label();
		mv.visitLabel(l1);
		mv.visitLineNumber(82, l1);
		mv.visitInsn(RETURN);
		Label l2 = new Label();
		mv.visitLabel(l2);
		mv.visitLocalVariable("this", "Llumien/randomthings/Transformer/RTClassTransformer;", null, l0, l2, 0);
		mv.visitLocalVariable("p_149695_1_", "L" + worldClass + ";", null, l0, l2, 1);
		mv.visitLocalVariable("p_149695_2_", "I", null, l0, l2, 2);
		mv.visitLocalVariable("p_149695_3_", "I", null, l0, l2, 3);
		mv.visitLocalVariable("p_149695_4_", "I", null, l0, l2, 4);
		mv.visitLocalVariable("p_149695_5_", "L" + blockClass + ";", null, l0, l2, 5);
		mv.visitMaxs(5, 6);
		mv.visitEnd();

		return writer.toByteArray();
	}

}
