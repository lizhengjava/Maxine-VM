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
package com.sun.max.tele.debug;

import java.io.*;
import java.util.*;

import com.sun.max.jdwp.vm.data.*;
import com.sun.max.lang.*;
import com.sun.max.program.*;
import com.sun.max.tele.*;
import com.sun.max.tele.util.*;
import com.sun.max.unsafe.*;
import com.sun.max.util.*;

/**
 * Abstract base class for caching the values of a set of ISA defined registers for a given thread.
 *
 * @author Bernd Mathiske
 * @author Mick Jordan
 * @author Doug Simon
 * @author Michael Van De Vanter
 */
abstract class TeleRegisters extends AbstractTeleVMHolder implements TeleVMCache {

    private static final int TRACE_VALUE = 2;

    private final TimedTrace updateTracer;

    private final TeleRegisterSet teleRegisterSet;
    final Endianness endianness;
    private final Symbolizer<? extends Symbol> symbolizer;

    private final Address[] registerValues;
    private final byte[] registerData;
    private final ByteArrayInputStream registerDataInputStream;

    protected TeleRegisters(TeleVM teleVM, TeleRegisterSet teleRegisterSet, Symbolizer<? extends Symbol> symbolizer) {
        super(teleVM);
        final TimedTrace tracer = new TimedTrace(TRACE_VALUE, tracePrefix() + teleRegisterSet.thread().entityName() + " creating");
        tracer.begin();

        this.teleRegisterSet = teleRegisterSet;
        this.symbolizer = symbolizer;
        this.endianness = teleVM.vmConfiguration().platform().processorKind.dataModel.endianness;
        this.registerValues = new Address[symbolizer.numberOfValues()];
        this.registerData = new byte[symbolizer.numberOfValues() * Address.size()];
        this.registerDataInputStream = new ByteArrayInputStream(registerData);
        Arrays.fill(this.registerValues, Address.zero());

        this.updateTracer = new TimedTrace(TRACE_VALUE, tracePrefix() + teleRegisterSet.thread().entityName() + " updating");

        tracer.end(null);
    }

    public final void updateCache() {
        updateTracer.begin();
        // Refreshes the register values from the {@linkplain #registerData() raw buffer} holding the registers' values.
        // This method should be called whenever the raw buffer is updated.
        registerDataInputStream.reset();
        for (int i = 0; i != registerValues.length; i++) {
            try {
                registerValues[i] = Word.read(registerDataInputStream, endianness).asAddress();
            } catch (IOException ioException) {
                ProgramError.unexpected(ioException);
            }
        }
        updateTracer.end(null);
    }

    /**
     * Gets the value of a given register.
     *
     * @param register the register whose value is to be returned
     * @return the value of {@code register}
     */
    final Address getValue(Symbol register) {
        return registerValues[register.value()];
    }

    /**
     * Determines whether a particular register is the instruction pointer.
     *
     * @param register
     * @return whether the register is the instruction pointer
     */
    boolean isInstructionPointerRegister(Symbol register) {
        return false;
    }

    /**
     * Determines whether a particular register is a flags register.
     *
     * @param register
     * @return whether the register is a flags register
     */
    boolean isFlagsRegister(Symbol register) {
        return false;
    }

    /**
     * Gets the raw buffer into which the registers' values are read from the remote process.
     */
    final byte[] registerData() {
        return registerData;
    }

    final Symbolizer<? extends Symbol> symbolizer() {
        return symbolizer;
    }

    Address getValueAt(int index) {
        return registerValues[index];
    }

    /**
     * Sets the value of a given register.
     *
     * Note: This call only updates the value of the register in this cache. The update to the actual register in the
     * remote process must be done by the caller of this method.
     *
     * @param register the register whose value is to be updated
     * @param value the new value of {@code register}
     */
    protected final void setValue(Symbol register, Address value) {
        registerValues[register.value()] = value;
    }

    Registers getRegisters(String name) {
        final String[] registerNames = new String[symbolizer().numberOfValues()];
        final long[] values = new long[registerNames.length];
        int z = 0;
        for (Symbol s : symbolizer()) {
            registerNames[z] = s.name();
            values[z] = getValue(s).toLong();
            z++;
        }

        final Registers result = new Registers(name, registerNames, values);
        return result;
    }
}
