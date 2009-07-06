/*
 * Copyright (c) 2009 Sun Microsystems, Inc.  All rights reserved.
 *
 * Sun Microsystems, Inc. has intellectual property rights relating to technology embodied in the product
 * that is described in this document. In particular, and without limitation, these intellectual property
 * rights may include one or more of the U.S. patents listed at http://www.sun.com/patents and one or
 * more additional patents or pending patent applications in the U.S. and in other countries.
 *
 * U.S. Government Rights - Commercial software. Government users are subject to the Sun
 * Microsystems, Inc. standard license agreement and applicable provisions of the FAR and its
 * supplements.
 *
 * Use is subject to license terms. Sun, Sun Microsystems, the Sun logo, Java and Solaris are trademarks or
 * registered trademarks of Sun Microsystems, Inc. in the U.S. and other countries. All SPARC trademarks
 * are used under license and are trademarks or registered trademarks of SPARC International, Inc. in the
 * U.S. and other countries.
 *
 * UNIX is a registered trademark in the U.S. and other countries, exclusively licensed through X/Open
 * Company, Ltd.
 */
package com.sun.c1x.ir;

import com.sun.c1x.util.InstructionVisitor;
import com.sun.c1x.util.Util;
import com.sun.c1x.ci.CiType;
import com.sun.c1x.value.ValueStack;
import com.sun.c1x.value.ValueType;
import com.sun.c1x.bytecode.Bytecodes;

/**
 * The <code>InstanceOf</code> instruction represents an instanceof test.
 *
 * @author Ben L. Titzer
 */
public class InstanceOf extends TypeCheck {

    /**
     * Constructs a new InstanceOf instruction.
     * @param targetClass the target class of the instanceof check
     * @param object the instruction producing the object input to this instruction
     * @param stateBefore the state before this instruction
     */
    public InstanceOf(CiType targetClass, Instruction object, ValueStack stateBefore) {
        super(targetClass, object, ValueType.INT_TYPE, stateBefore);
    }

    /**
     * Implements this instruction's half of the visitor pattern.
     * @param v the visitor to accept
     */
    @Override
    public void accept(InstructionVisitor v) {
        v.visitInstanceOf(this);
    }

    @Override
    public int valueNumber() {
        return Util.hash1(Bytecodes.INSTANCEOF, object);
    }

    @Override
    public boolean valueEqual(Instruction i) {
        if (i instanceof InstanceOf) {
            InstanceOf o = (InstanceOf) i;
            return targetClass == o.targetClass && object == o.object;
        }
        return false;
    }

}
