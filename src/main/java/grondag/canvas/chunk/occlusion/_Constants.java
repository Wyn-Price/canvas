package grondag.canvas.chunk.occlusion;

import grondag.canvas.Configurator;

public class _Constants {

	static final boolean ENABLE_RASTER_OUTPUT = Configurator.debugOcclusionRaster;
	static final int TILE_AXIS_SHIFT = 3;
	static final int TILE_PIXEL_DIAMETER = 1 << TILE_AXIS_SHIFT;
	static final int TILE_PIXEL_INDEX_MASK = TILE_PIXEL_DIAMETER - 1;
	static final int TILE_PIXEL_INVERSE_MASK = ~TILE_PIXEL_INDEX_MASK;
	static final int LOW_AXIS_SHIFT = TILE_AXIS_SHIFT;
	static final int MID_AXIS_SHIFT = TILE_AXIS_SHIFT * 2;
	static final int MID_INDEX_SHIFT = LOW_AXIS_SHIFT * 2;
	static final int TOP_INDEX_SHIFT = MID_INDEX_SHIFT * 2;

	static final int MID_WIDTH = 16;
	static final int MID_Y_SHIFT = Integer.bitCount(MID_WIDTH - 1);
	static final int MIDDLE_HEIGHT = 8;

	static final int TOP_Y_SHIFT = Integer.bitCount(MID_WIDTH / 8 - 1);

	static final int PRECISION_BITS = 4;
	static final int PRECISE_FRACTION_MASK = (1 << PRECISION_BITS) - 1;
	static final int PRECISE_INTEGER_MASK = ~PRECISE_FRACTION_MASK;
	static final int PRECISE_PIXEL_SIZE = 1 << PRECISION_BITS;
	static final int PRECISE_PIXEL_CENTER = PRECISE_PIXEL_SIZE / 2;

	static final int LOW_WIDTH = MID_WIDTH * 8;
	//static final int LOW_Y_SHIFT = Integer.bitCount(LOW_WIDTH - 1);
	static final int PIXEL_WIDTH = LOW_WIDTH * TILE_PIXEL_DIAMETER;
	static final int MAX_PIXEL_X = PIXEL_WIDTH - 1;
	static final int HALF_PIXEL_WIDTH = PIXEL_WIDTH / 2;
	static final int PRECISE_WIDTH = PIXEL_WIDTH << PRECISION_BITS;
	static final int HALF_PRECISE_WIDTH = PRECISE_WIDTH / 2;
	/** clamp to this to ensure value + half pixel rounds down to last pixel */
	static final int PRECISE_WIDTH_CLAMP = PRECISE_WIDTH - PRECISE_PIXEL_CENTER;

	static final int LOW_HEIGHT = MIDDLE_HEIGHT * 8;
	static final int PIXEL_HEIGHT = LOW_HEIGHT * TILE_PIXEL_DIAMETER;
	static final int MAX_PIXEL_Y = PIXEL_HEIGHT - 1;
	static final int HALF_PIXEL_HEIGHT = PIXEL_HEIGHT / 2;
	//	static final int HEIGHT_WORD_RELATIVE_SHIFT = LOW_Y_SHIFT - BIN_AXIS_SHIFT;
	static final int PRECISE_HEIGHT = PIXEL_HEIGHT << PRECISION_BITS;
	static final int HALF_PRECISE_HEIGHT = PRECISE_HEIGHT / 2;
	/** clamp to this to ensure value + half pixel rounds down to last pixel */
	static final int PRECISE_HEIGHT_CLAMP = PRECISE_HEIGHT - PRECISE_PIXEL_CENTER;

	static final int GUARD_SIZE = 512 << PRECISION_BITS;
	static final int GUARD_WIDTH = PRECISE_WIDTH + GUARD_SIZE;
	static final int GUARD_HEIGHT = PRECISE_HEIGHT + GUARD_SIZE;

	static final int LOW_TILE_COUNT = LOW_WIDTH * LOW_HEIGHT;
	static final int MID_TILE_COUNT = MID_WIDTH * LOW_HEIGHT;

	static final int MID_TILE_PIXEL_DIAMETER = PIXEL_WIDTH / MID_WIDTH;
	static final int MID_TILE_PIXEL_INDEX_MASK = MID_TILE_PIXEL_DIAMETER - 1;

	static final int LOW_TILE_PIXEL_DIAMETER = PIXEL_WIDTH / LOW_WIDTH;
	static final int LOW_TILE_PIXEL_INDEX_MASK = LOW_TILE_PIXEL_DIAMETER - 1;

	static final long[] EMPTY_BITS = new long[LOW_TILE_COUNT];

	static final int CAMERA_PRECISION_BITS = 12;
	static final int CAMERA_PRECISION_UNITY = 1 << CAMERA_PRECISION_BITS;
	static final int CAMERA_PRECISION_CHUNK_MAX = 16 * CAMERA_PRECISION_UNITY;
}
