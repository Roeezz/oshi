/**
 * MIT License
 *
 * Copyright (c) 2010 - 2020 The OSHI Project Contributors:
 * https://github.com/oshi/oshi/graphs/contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package oshi.software.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystemVersion;

public abstract class AbstractOperatingSystem implements OperatingSystem {

    private static final long serialVersionUID = 1L;

    protected String manufacturer;
    protected String family;
    protected OperatingSystemVersion version;
    // Initialize based on JVM Bitness. Individual OS implementations will test
    // if 32-bit JVM running on 64-bit OS
    protected int bitness = System.getProperty("os.arch").indexOf("64") != -1 ? 64 : 32;

    /*
     * Comparators for use in processSort()
     */
    private static final Comparator<OSProcess> CPU_DESC_SORT = new Comparator<OSProcess>() {
        @Override
        public int compare(OSProcess p1, OSProcess p2) {
            return Double.compare(p2.calculateCpuPercent(), p1.calculateCpuPercent());
        }
    };
    private static final Comparator<OSProcess> RSS_DESC_SORT = new Comparator<OSProcess>() {
        @Override
        public int compare(OSProcess p1, OSProcess p2) {
            return Long.compare(p2.getResidentSetSize(), p1.getResidentSetSize());
        }
    };
    private static final Comparator<OSProcess> UPTIME_DESC_SORT = new Comparator<OSProcess>() {
        @Override
        public int compare(OSProcess p1, OSProcess p2) {
            return Long.compare(p2.getUpTime(), p1.getUpTime());
        }
    };
    private static final Comparator<OSProcess> UPTIME_ASC_SORT = new Comparator<OSProcess>() {
        @Override
        public int compare(OSProcess p1, OSProcess p2) {
            return Long.compare(p1.getUpTime(), p2.getUpTime());
        }
    };
    private static final Comparator<OSProcess> PID_ASC_SORT = new Comparator<OSProcess>() {
        @Override
        public int compare(OSProcess p1, OSProcess p2) {
            return Integer.compare(p1.getProcessID(), p2.getProcessID());
        }
    };
    private static final Comparator<OSProcess> PARENTPID_ASC_SORT = new Comparator<OSProcess>() {
        @Override
        public int compare(OSProcess p1, OSProcess p2) {
            return Integer.compare(p1.getParentProcessID(), p2.getParentProcessID());
        }
    };
    private static final Comparator<OSProcess> NAME_ASC_SORT = new Comparator<OSProcess>() {
        @Override
        public int compare(OSProcess p1, OSProcess p2) {
            return p1.getName().toLowerCase().compareTo(p2.getName().toLowerCase());
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public OperatingSystemVersion getVersion() {
        return this.version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFamily() {
        return this.family;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getManufacturer() {
        return this.manufacturer;
    }

    /**
     * Sorts an array of processes using the specified sorting, returning an
     * array with the top limit results if positive.
     *
     * @param processes
     *            The array to sort
     * @param limit
     *            The number of results to return if positive; if zero returns
     *            all results
     * @param sort
     *            The sorting to use, or null
     * @return An array of size limit (if positive) or of all processes, sorted
     *         as specified
     */
    protected List<OSProcess> processSort(List<OSProcess> processes, int limit, ProcessSort sort) {
        if (sort != null) {
            switch (sort) {
            case CPU:
                Collections.sort(processes, CPU_DESC_SORT);
                break;
            case MEMORY:
                Collections.sort(processes, RSS_DESC_SORT);
                break;
            case OLDEST:
                Collections.sort(processes, UPTIME_DESC_SORT);
                break;
            case NEWEST:
                Collections.sort(processes, UPTIME_ASC_SORT);
                break;
            case PID:
                Collections.sort(processes, PID_ASC_SORT);
                break;
            case PARENTPID:
                Collections.sort(processes, PARENTPID_ASC_SORT);
                break;
            case NAME:
                Collections.sort(processes, NAME_ASC_SORT);
                break;
            default:
                // Should never get here! If you get this exception you've
                // added something to the enum without adding it here. Tsk.
                throw new IllegalArgumentException("Unimplemented enum type: " + sort.toString());
            }
        }
        // Return max of limit or process size
        // Nonpositive limit means return all
        int maxProcs = processes.size();
        if (limit > 0 && maxProcs > limit) {
            maxProcs = limit;
        } else {
            return processes;
        }
        List<OSProcess> procs = new ArrayList<>();
        for (int i = 0; i < maxProcs; i++) {
            procs.add(processes.get(i));
        }
        return procs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getManufacturer()).append(' ').append(getFamily()).append(' ').append(getVersion().toString());
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OSProcess[] getProcesses(int limit, ProcessSort sort) {
        return getProcesses(limit, sort, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OSProcess> getProcesses(Collection<Integer> pids) {
        List<OSProcess> returnValue = new ArrayList<>(pids.size());
        for (Integer pid : pids) {
            OSProcess process = getProcess(pid);
            if (process != null) {
                returnValue.add(process);
            }
        }
        return returnValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getBitness() {
        return this.bitness;
    }
}
