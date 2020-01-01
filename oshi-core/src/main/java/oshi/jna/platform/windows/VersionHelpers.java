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
package oshi.jna.platform.windows;

import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.WORD;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinNT.OSVERSIONINFOEX;

/**
 * The following functions can be used to determine the current operating system
 * version or identify whether it is a Windows or Windows Server release. These
 * functions provide simple tests that use the VerifyVersionInfo function and
 * the recommended greater than or equal to comparisons that are proven as a
 * robust means to determine the operating system version.
 */
public class VersionHelpers {

    /**
     * This function is useful in confirming a version of Windows Server that
     * doesn't share a version number with a client release. You should only use
     * this function if the other provided version helper functions do not fit
     * your scenario.
     * 
     * @param wMajorVersion
     *            The major version to test
     * @param wMinorVersion
     *            The minor version to test
     * @param wServicePackMajor
     *            The service pack to test
     * @return True if the current OS version matches, or is greater than, the
     *         provided version information.
     */
    public static boolean IsWindowsVersionOrGreater(int wMajorVersion, int wMinorVersion, int wServicePackMajor) {
        OSVERSIONINFOEX osvi = new OSVERSIONINFOEX();
        osvi.dwOSVersionInfoSize = new DWORD(osvi.size());
        osvi.dwMajorVersion = new DWORD(wMajorVersion);
        osvi.dwMinorVersion = new DWORD(wMinorVersion);
        osvi.wServicePackMajor = new WORD(wServicePackMajor);

        long dwlConditionMask = 0;
        dwlConditionMask = Kernel32.INSTANCE.VerSetConditionMask(dwlConditionMask, WinNT.VER_MAJORVERSION,
                (byte) WinNT.VER_GREATER_EQUAL);
        dwlConditionMask = Kernel32.INSTANCE.VerSetConditionMask(dwlConditionMask, WinNT.VER_MINORVERSION,
                (byte) WinNT.VER_GREATER_EQUAL);
        dwlConditionMask = Kernel32.INSTANCE.VerSetConditionMask(dwlConditionMask, WinNT.VER_SERVICEPACKMAJOR,
                (byte) WinNT.VER_GREATER_EQUAL);

        return Kernel32.INSTANCE.VerifyVersionInfoW(osvi,
                WinNT.VER_MAJORVERSION | WinNT.VER_MINORVERSION | WinNT.VER_SERVICEPACKMAJOR, dwlConditionMask);
    }

    /**
     * @return true if the current OS version matches, or is greater than, the
     *         Windows XP version.
     */
    public static boolean IsWindowsXPOrGreater() {
        return IsWindowsVersionOrGreater(HIBYTE(Kernel32.WIN32_WINNT_WINXP), LOBYTE(Kernel32.WIN32_WINNT_WINXP), 0);
    }

    /**
     * @return true if the current OS version matches, or is greater than, the
     *         Windows XP with Service Pack 1 (SP1) version.
     */
    public static boolean IsWindowsXPSP1OrGreater() {
        return IsWindowsVersionOrGreater(HIBYTE(Kernel32.WIN32_WINNT_WINXP), LOBYTE(Kernel32.WIN32_WINNT_WINXP), 1);
    }

    /**
     * @return true if the current OS version matches, or is greater than, the
     *         Windows XP with Service Pack 2 (SP2) version.
     */
    public static boolean IsWindowsXPSP2OrGreater() {
        return IsWindowsVersionOrGreater(HIBYTE(Kernel32.WIN32_WINNT_WINXP), LOBYTE(Kernel32.WIN32_WINNT_WINXP), 2);
    }

    /**
     * @return true if the current OS version matches, or is greater than, the
     *         Windows XP with Service Pack 3 (SP3) version.
     */
    public static boolean IsWindowsXPSP3OrGreater() {
        return IsWindowsVersionOrGreater(HIBYTE(Kernel32.WIN32_WINNT_WINXP), LOBYTE(Kernel32.WIN32_WINNT_WINXP), 3);
    }

    /**
     * @return true if the current OS version matches, or is greater than, the
     *         Windows Vista version.
     */
    public static boolean IsWindowsVistaOrGreater() {
        return IsWindowsVersionOrGreater(HIBYTE(Kernel32.WIN32_WINNT_VISTA), LOBYTE(Kernel32.WIN32_WINNT_VISTA), 0);
    }

    /**
     * @return true if the current OS version matches, or is greater than, the
     *         Windows Vista with Service Pack 1 (SP1) version.
     */
    public static boolean IsWindowsVistaSP1OrGreater() {
        return IsWindowsVersionOrGreater(HIBYTE(Kernel32.WIN32_WINNT_VISTA), LOBYTE(Kernel32.WIN32_WINNT_VISTA), 1);
    }

    /**
     * @return true if the current OS version matches, or is greater than, the
     *         Windows Vista with Service Pack 2 (SP2) version.
     */
    public static boolean IsWindowsVistaSP2OrGreater() {
        return IsWindowsVersionOrGreater(HIBYTE(Kernel32.WIN32_WINNT_VISTA), LOBYTE(Kernel32.WIN32_WINNT_VISTA), 2);
    }

    /**
     * @return true if the current OS version matches, or is greater than, the
     *         Windows 7 version.
     */
    public static boolean IsWindows7OrGreater() {
        return IsWindowsVersionOrGreater(HIBYTE(Kernel32.WIN32_WINNT_WIN7), LOBYTE(Kernel32.WIN32_WINNT_WIN7), 0);
    }

    /**
     * @return true if the current OS version matches, or is greater than, the
     *         Windows 7 with Service Pack 1 (SP1) version.
     */
    public static boolean IsWindows7SP1OrGreater() {
        return IsWindowsVersionOrGreater(HIBYTE(Kernel32.WIN32_WINNT_WIN7), LOBYTE(Kernel32.WIN32_WINNT_WIN7), 1);
    }

    /**
     * @return true if the current OS version matches, or is greater than, the
     *         Windows 8 version.
     */
    public static boolean IsWindows8OrGreater() {
        return IsWindowsVersionOrGreater(HIBYTE(Kernel32.WIN32_WINNT_WIN8), LOBYTE(Kernel32.WIN32_WINNT_WIN8), 0);
    }

    /**
     * @return true if the current OS version matches, or is greater than, the
     *         Windows 8.1 version. For Windows 10, IsWindows8Point1OrGreater
     *         returns false unless the application contains a manifest that
     *         includes a compatibility section that contains the GUIDs that
     *         designate Windows 8.1 and/or Windows 10.
     */
    public static boolean IsWindows8Point1OrGreater() {
        return IsWindowsVersionOrGreater(HIBYTE(Kernel32.WIN32_WINNT_WINBLUE), LOBYTE(Kernel32.WIN32_WINNT_WINBLUE), 0);
    }

    /**
     * @return true if the current OS version matches, or is greater than, the
     *         Windows 10 version. For Windows 10, IsWindows10OrGreater returns
     *         false unless the application contains a manifest that includes a
     *         compatibility section that contains the GUID that designates
     *         Windows 10.
     */
    public static boolean IsWindows10OrGreater() {
        return IsWindowsVersionOrGreater(HIBYTE(Kernel32.WIN32_WINNT_WIN10), LOBYTE(Kernel32.WIN32_WINNT_WIN10), 0);
    }

    /**
     * Applications that need to distinguish between server and client versions
     * of Windows should call this function.
     * 
     * @return true if the current OS is a Windows Server release.
     */
    public static boolean IsWindowsServer() {
        OSVERSIONINFOEX osvi = new OSVERSIONINFOEX();
        osvi.dwOSVersionInfoSize = new DWORD(osvi.size());
        osvi.wProductType = WinNT.VER_NT_WORKSTATION;

        long dwlConditionMask = Kernel32.INSTANCE.VerSetConditionMask(0, WinNT.VER_PRODUCT_TYPE,
                (byte) WinNT.VER_EQUAL);

        return !Kernel32.INSTANCE.VerifyVersionInfoW(osvi, WinNT.VER_PRODUCT_TYPE, dwlConditionMask);
    }

    /**
     * Get the high byte
     * 
     * @param word
     *            a two-byte value
     * @return The most significant byte
     */
    private static byte HIBYTE(short word) {
        return (byte) ((word >> 8) & 0xFF);
    }

    /**
     * Get the low byte
     * 
     * @param word
     *            a two-byte value
     * @return The least significant byte
     */
    private static byte LOBYTE(short word) {
        return (byte) word;
    }
}
