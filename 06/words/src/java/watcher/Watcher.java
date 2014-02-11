package watcher;

import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;

public class Watcher {
	public static void register(String filename) throws IOException {
		WatchService watcher = FileSystems.getDefault().newWatchService();
		Path dir = Paths.get(filename);

		WatchKey key = dir.register(watcher, ENTRY_MODIFY);

		WatchKey k;

		while(true) {
			k = watcher.take();
		}

	}

}