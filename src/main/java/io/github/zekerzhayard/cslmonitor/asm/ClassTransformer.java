package io.github.zekerzhayard.cslmonitor.asm;

import com.mojang.authlib.GameProfile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import customskinloader.config.SkinSiteProfile;
import customskinloader.profile.UserProfile;
import io.github.zekerzhayard.cslmonitor.CSLMonitor;
import io.github.zekerzhayard.cslmonitor.CSLThreadPoolMonitor;
import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer {
    private Logger logger = LogManager.getLogger(CSLMonitor.NAME);

    @Override()
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("customskinloader.CustomSkinLoader")) {
            this.logger.debug("Found the class: " + transformedName);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            new ClassReader(basicClass).accept(new ClassVisitor(Opcodes.ASM5, cw) {
                @Override()
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                    if (name.equals("loadProfile0") && desc.equals("(L" + GameProfile.class.getName().replace('.', '/') + ";)L" + UserProfile.class.getName().replace('.', '/') + ";")) {
                        ClassTransformer.this.logger.debug("Found the Method: " + name + desc);
                        return new MethodVisitor(Opcodes.ASM5, mv) {
                            private String ctpmClName = CSLThreadPoolMonitor.class.getName().replace('.', '/');

                            @Override()
                            public void visitTypeInsn(int opcode, String type) {
                                this.mv.visitTypeInsn(opcode, type);
                                String sspClName = SkinSiteProfile.class.getName().replace('.', '/');
                                if (opcode == Opcodes.CHECKCAST && type.equals(sspClName)) {
                                    ClassTransformer.this.logger.debug("Found the insn: {} {}", opcode, type);
                                    this.mv.visitMethodInsn(Opcodes.INVOKESTATIC, ctpmClName, "put", "(L" + sspClName + ";)L" + sspClName + ";", false);
                                }
                            }

                            @Override()
                            public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                                this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
                                if (opcode == Opcodes.INVOKEVIRTUAL && owner.equals(UserProfile.class.getName().replace('.', '/')) && name.equals("isEmpty") && desc.equals("()Z")) {
                                    ClassTransformer.this.logger.debug("Found the insn: {} {}.{}{}", opcode, owner, name, desc);
                                    this.mv.visitMethodInsn(Opcodes.INVOKESTATIC, ctpmClName, "remove", "(Z)Z", false);
                                }
                            }
                        };
                    }
                    return mv;
                }
            }, ClassReader.EXPAND_FRAMES);
            return cw.toByteArray();
        }
        return basicClass;
    }
}