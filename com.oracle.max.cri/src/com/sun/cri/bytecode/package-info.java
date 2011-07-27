/*
 * Copyright (c) 2010, 2011, Oracle and/or its affiliates. All rights reserved.
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

/**
 * A collection of classes that support the processing of bytecodes.
 * The majority are merely utility classes that simplify and reduce errors during bytecode processing,
 * whereas, {@link com.sun.cri.bytecode.INTRINSIC} and {@link com.sun.cri.bytecode.BytecodeIntrinsifier}
 * provide the capability to transform the bytecodes of a method by replacing certain bytecode instructions
 * with alternatives drawn from a set of <i>extended</i> bytecodes.
 *
 */
package com.sun.cri.bytecode;

/**
 */
