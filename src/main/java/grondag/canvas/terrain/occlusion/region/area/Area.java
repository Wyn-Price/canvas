package grondag.canvas.terrain.occlusion.region.area;

import grondag.canvas.terrain.occlusion.region.OcclusionBitPrinter;

public class Area {
	public final int areaKey;
	public final int index;

	public static boolean isIncludedBySample(long[] sample, int sampleStart, int areaKey) {
		final long template = bits(areaKey, 0);
		final long template1 = bits(areaKey, 1);
		final long template2 = bits(areaKey, 2);
		final long template3 = bits(areaKey, 3);

		return (template & sample[sampleStart]) == template
				&& (template1 & sample[sampleStart + 1]) == template1
				&& (template2 & sample[sampleStart + 2]) == template2
				&& (template3 & sample[sampleStart + 3]) == template3;
	}

	public static boolean intersects(int areaKeyA, int areaKeyB) {
		return (bits(areaKeyA, 0) & bits(areaKeyB, 0)) != 0
				|| (bits(areaKeyA, 1) & bits(areaKeyB, 1)) != 0
				|| (bits(areaKeyA, 2) & bits(areaKeyB, 2)) != 0
				|| (bits(areaKeyA, 3) & bits(areaKeyB, 3)) != 0;
	}

	public static boolean intersectsWithSample(long[] sample, int sampleStart, int areaKey) {
		return (bits(areaKey, 0) & sample[sampleStart]) != 0
				|| (bits(areaKey, 1) & sample[++sampleStart]) != 0
				|| (bits(areaKey, 2) & sample[++sampleStart]) != 0
				|| (bits(areaKey, 3) & sample[++sampleStart]) != 0;
	}

	public static boolean isAdditive(long[] sample, int sampleStart, int areaKey) {
		return (bits(areaKey, 0) | sample[sampleStart]) != sample[sampleStart]
				|| (bits(areaKey, 1) | sample[++sampleStart]) != sample[sampleStart]
						|| (bits(areaKey, 2) | sample[++sampleStart]) != sample[sampleStart]
								|| (bits(areaKey, 3) | sample[++sampleStart]) != sample[sampleStart];
	}

	public static void setBits(long[] targetBits, int startIndex, int areaKey) {
		targetBits[startIndex] |= bits(areaKey, 0);
		targetBits[++startIndex] |= bits(areaKey, 1);
		targetBits[++startIndex] |= bits(areaKey, 2);
		targetBits[++startIndex] |= bits(areaKey, 3);
	}

	public static void clearBits(long[] targetBits, int startIndex, int areaKey) {
		targetBits[startIndex] &= ~bits(areaKey, 0);
		targetBits[++startIndex] &= ~bits(areaKey, 1);
		targetBits[++startIndex] &= ~bits(areaKey, 2);
		targetBits[++startIndex] &= ~bits(areaKey, 3);
	}

	public Area(int rectKey, int index) {
		areaKey = rectKey;
		this.index = index;
	}

	private static int rowMask(int areaKey) {
		return (0xFFFF << AreaUtil.x0(areaKey)) & (0xFFFF >> (15 - AreaUtil.x1(areaKey)));
	}

	private static long bits(int areaKey, int y) {
		final int yMin = y << 2;
		final int yMax = yMin + 3;

		final int y0 = Math.max(yMin, AreaUtil.y0(areaKey));
		final int y1 = Math.min(yMax, AreaUtil.y1(areaKey));

		if (y0 > y1) {
			return 0L;
		}

		long result = 0;
		final long mask = rowMask(areaKey);

		final int limit = y1 & 3;

		for (int i = y0 & 3; i <= limit; ++i) {
			result |= (mask << (i <<4));
		}

		return result;
	}

	public static void printShape(int areaKey) {
		final long[] bits = new long[4];
		bits[0] = bits(areaKey, 0);
		bits[1] = bits(areaKey, 1);
		bits[2] = bits(areaKey, 2);
		bits[3] = bits(areaKey, 3);

		OcclusionBitPrinter.printShape(bits, 0);
	}
}