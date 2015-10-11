package org.evilbinary.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class BlockFile<T> implements Iterable<List<T>> {

	private int BUFFER_SIZE = 1024;// 缓冲区大小为1k
	private long CHUNK_SIZE = 0x300000;
	private Iterator<File> files;

	private long chunkPos = 0;
	private MappedByteBuffer buffer;
	private FileChannel channel;

	public BlockFile(File... files) {
		this(Arrays.asList(files));
	}

	public BlockFile(List<File> files) {
		this.files = files.iterator();
	}

	public Iterator<List<T>> iterator() {
		return new Iterator<List<T>>() {
			private List<T> entries;
			private long chunkPos = 0;
			private MappedByteBuffer buffer;
			private FileChannel channel;

			@Override
			public boolean hasNext() {
				if (buffer == null || !buffer.hasRemaining()) {
					buffer = nextBuffer(chunkPos);
					if (buffer == null) {
						return false;
					}
				}
				T result = null;
				while ((result =(T) buffer ) != null) {
					if (entries == null) {
						entries = new ArrayList<T>();
					}
					entries.add(result);
				}
				// set next MappedByteBuffer chunk
				chunkPos += buffer.position();
				buffer = null;
				if (entries != null) {
					return true;
				} else {
					// Closeables.closeQuietly(channel);
					return false;
				}
			}

			private MappedByteBuffer nextBuffer(long position) {
				try {
					if (channel == null || channel.size() == position) {
						if (channel != null) {
							// Closeables.closeQuietly(channel);
							channel = null;
						}
						if (files.hasNext()) {
							File file = files.next();
							channel = new RandomAccessFile(file, "r")
									.getChannel();
							chunkPos = 0;
							position = 0;
						} else {
							return null;
						}
					}
					long chunkSize = CHUNK_SIZE;
					if (channel.size() - position < chunkSize) {
						chunkSize = channel.size() - position;
					}
					return channel.map(FileChannel.MapMode.READ_ONLY, chunkPos,
							chunkSize);
				} catch (IOException e) {
					// Closeables.closeQuietly(channel);
					throw new RuntimeException(e);
				}
			}

			@Override
			public List<T> next() {
				List<T> res = entries;
				entries = null;
				return res;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};

	}
}
