/*
 * Copyright (c) 2009, 2011, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.max.graal.nodes.java;

import com.oracle.max.graal.nodes.*;
import com.oracle.max.graal.nodes.extended.*;
import com.oracle.max.graal.nodes.type.*;

/**
 * The {@code AccessMonitorNode} is the base class of both monitor acquisition and release.
 */
public abstract class AccessMonitorNode extends AbstractStateSplit implements MemoryCheckpoint {

    @Input private ValueNode object;
    @Data private int monitorIndex;
    @Data private final boolean monitorStackSlots;

    public ValueNode object() {
        return object;
    }

    public int monitorIndex() {
        return monitorIndex;
    }


    public void setMonitorIndex(int monitorIndex) {
        this.monitorIndex = monitorIndex;
    }

    public boolean monitorStackSlots() {
        return monitorStackSlots;
    }

    /**
     * Creates a new AccessMonitor instruction.
     *
     * @param object the instruction producing the object
     * @param lockAddress the address of the on-stack lock object or {@code null} if the runtime does not place locks on the stack
     * @param lockNumber the number of the lock being acquired
     * @param monitorStackSlots determines if space on the stack should be reserved for each monitor
     */
    public AccessMonitorNode(ValueNode object, int monitorIndex, boolean monitorStackSlots) {
        super(StampFactory.illegal());
        this.object = object;
        this.monitorIndex = monitorIndex;
        this.monitorStackSlots = monitorStackSlots;
    }
}