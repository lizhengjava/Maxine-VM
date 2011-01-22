/*
 * Copyright (c) 2007, 2010, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.sun.max.asm.gen.cisc.x86;

import java.util.*;

import com.sun.max.asm.*;
import com.sun.max.asm.gen.*;

/**
 * @author Bernd Mathiske
 */
public enum FloatingPointOperandCode implements WrappableSpecification {
    bytes_2(""),
    bytes_14_28(""),
    bytes_98_108(""),
    word_integer("s"),
    short_integer("l"),
    long_integer("q"),
    single_real("s"),
    double_real("l"),
    extended_real("t"),
    packed_bcd(""),
    ST_i("");

    private final String operandTypeSuffix;

    private FloatingPointOperandCode(String operandTypeSuffix) {
        this.operandTypeSuffix = operandTypeSuffix;
    }

    public String operandTypeSuffix() {
        return operandTypeSuffix;
    }

    public TestArgumentExclusion excludeExternalTestArguments(Argument... arguments) {
        return new TestArgumentExclusion(AssemblyTestComponent.EXTERNAL_ASSEMBLER, this, new HashSet<Argument>(Arrays.asList(arguments)));
    }
}
