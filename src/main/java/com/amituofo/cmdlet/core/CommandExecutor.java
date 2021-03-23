package com.amituofo.cmdlet.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;

public class CommandExecutor {
	private static FileLock lock;

	public static <TC extends Command<TARG>, TARG extends CommandArguments> int exec(Class<TC> dgc, String[] args) {
		return exec(dgc, args, false);
	}
	
	public static <TC extends Command<TARG>, TARG extends CommandArguments> int exec(Class<TC> dgc, String[] args, boolean oneInstance) {
		Command<TARG> cmd = null;
		try {
			cmd = build(dgc);
			
			cmd.start(args, oneInstance);
		} catch (CommandException e) {
			// Command.out.info(e.getMessage());
			Command.console.println(e.getMessage());
			Command.console.println(e.getCause().getMessage());
			// e.printStackTrace();
			return e.getExitCode();
		}

		if (cmd.exitResult != null) {
			return cmd.exitResult.exitCode;
		} else {
			return ExitResult.COMPLETE.exitCode;
		}
	}

	public <TC extends Command<TARG>, TARG extends CommandArguments> int exec(TC cmd, String[] args, boolean oneInstance) throws CommandException {

		cmd.exitResult = cmd.start(args, oneInstance);

		if (cmd.exitResult != null) {
			return cmd.exitResult.exitCode;
		} else {
			return ExitResult.COMPLETE.exitCode;
		}
	}

	public static <TC extends Command<TARG>, TARG extends CommandArguments> Command<TARG> build(Class<TC> dgc) throws CommandException {
		Command<TARG> cmd = null;

		try {
			cmd = dgc.newInstance();
		} catch (Throwable e) {
			throw new CommandException("Exception occured when execute command! ", e);
		}

//		try {
//			exec(cmd, cmdArg, oneInstance);
//		} catch (CommandException e) {
//			throw new CommandException("Exception occured when start service! ", e);
//		}

		return cmd;
	}

	public static boolean isRunning(Class<?> dgc) {
		FileOutputStream fo = null;
		String key = "INSTANCE-" + dgc.getSimpleName();
		try {
			File instanceLock = null;
			String folder = System.getProperty("java.io.tmpdir");
			if (folder == null || folder.length() == 0) {
				File tmp = File.createTempFile("TMP", "");
				folder = tmp.getParent();
				tmp.delete();
			}

			if (folder != null && folder.length() > 1) {
				if (folder.charAt(folder.length() - 1) != File.separatorChar) {
					folder += File.separatorChar;
				}
			}

			instanceLock = new File(folder + key);
			instanceLock.deleteOnExit();
			if (!instanceLock.exists())
				instanceLock.createNewFile();
			fo = new FileOutputStream(instanceLock);
			lock = fo.getChannel().tryLock();
			// if lock=null means the file has been locked,one instance is running
			return (lock == null);
		} catch (Throwable ex) {
			try {
				fo.close();
			} catch (IOException e) {
			}
			return false;
		}
	}
}
