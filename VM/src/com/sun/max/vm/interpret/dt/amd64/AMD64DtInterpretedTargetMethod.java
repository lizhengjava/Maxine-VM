/*
 * Copyright (c) 2007 Sun Microsystems, Inc.  All rights reserved.
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
package com.sun.max.vm.interpret.dt.amd64;

import com.sun.max.asm.*;
import com.sun.max.vm.actor.member.*;
import com.sun.max.vm.interpret.*;
import com.sun.max.vm.stack.*;

/**
 * A collection of objects that represent stub code and auxiliary
 * data structures necessary for interpreting a Java method.
 *
 * @author Simon Wilkinson
 */
public class AMD64DtInterpretedTargetMethod extends InterpretedTargetMethod {

    private final AMD64DtInterpreter interpreter;

    public AMD64DtInterpretedTargetMethod(ClassMethodActor classMethodActor, AMD64DtInterpreter interpreter) {
        super(classMethodActor);
        this.interpreter = interpreter;
    }

    @Override
    public InstructionSet instructionSet() {
        return InstructionSet.AMD64;
    }

    @Override
    public JavaStackFrameLayout stackFrameLayout() {
        return new AMD64DtInterpreterStackFrameLayout(classMethodActor(), interpreter);
    }

}
