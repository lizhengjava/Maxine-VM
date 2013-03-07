/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.max.vm.ext.graal.nodes;

import com.oracle.graal.api.meta.*;
import com.oracle.graal.nodes.*;
import com.oracle.graal.nodes.java.*;
import com.oracle.max.vm.ext.graal.*;

/**
 * Denotes an unresolved method call target.
 * The resolved {@link #resolvedMethodActor}, which is produced by an {@link ResolveMethod} node is
 * used in an {@link IndirectCallTargetNode}.
 *
 * In order to subclass {@link MethodCallTargetNode} and avoid replicating a lot of logic,
 * we fake a {@link ResolvedJavaMethod} that has just enough functionality to get by.
 * This means, of course, that {@link MethodCallTargetNode} does not really need an actual {@link ResolvedJavaMethod}.
 */
public class UnresolvedMethodCallTargetNode extends MethodCallTargetNode  {

    @Input ValueNode resolvedMethodActor;

    public UnresolvedMethodCallTargetNode(InvokeKind invokeKind, JavaMethod targetMethod,
                    ValueNode[] arguments, JavaType returnType, ValueNode methodValue) {
        super(invokeKind, MaxResolvedJavaMethod.getFake(targetMethod), arguments, returnType);
        this.resolvedMethodActor = methodValue;
    }

    public ValueNode resolvedMethodActor()  {
        return resolvedMethodActor;
    }


}
