/*******************************************************************************
 * Copyright 2020 grondag
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package grondag.canvas.pipeline;

import com.google.common.util.concurrent.Runnables;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;

import grondag.canvas.CanvasMod;
import grondag.canvas.Configurator;

public enum BufferDebug {
	NORMAL(Runnables.doNothing()),
	EMISSIVE(CanvasFrameBufferHacks::debugEmissive),
	BLOOM_0(() -> CanvasFrameBufferHacks.debugBlur(0)),
	BLOOM_1(() -> CanvasFrameBufferHacks.debugBlur(1)),
	BLOOM_2(() -> CanvasFrameBufferHacks.debugBlur(2)),
	BLOOM_3(() -> CanvasFrameBufferHacks.debugBlur(3)),
	BLOOM_4(() -> CanvasFrameBufferHacks.debugBlur(4)),
	BLOOM_5(() -> CanvasFrameBufferHacks.debugBlur(5)),
	BLOOM_6(() -> CanvasFrameBufferHacks.debugBlur(6));

	private final Runnable task;

	private BufferDebug(Runnable task) {
		this.task = task;
	}

	private static BufferDebug current = NORMAL;

	public static BufferDebug current() {
		return current;
	}

	public static void advance() {
		final BufferDebug[] values = values();
		current = values[(current.ordinal() + 1) % values.length];
	}

	/**
	 * Don't call unless enabled - doesn't check.
	 */
	@SuppressWarnings("resource")
	public static void render() {
		while (CanvasMod.VIEW_KEY.wasPressed()) {
			final long handle = MinecraftClient.getInstance().getWindow().getHandle();

			final int i = (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_SHIFT) || InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_RIGHT_SHIFT)) ? -1 : 1;
			final BufferDebug[] values = values();
			final int count =  values.length;

			current = values[(current.ordinal() + count + i) % values.length];

			MinecraftClient.getInstance().player.sendMessage(new LiteralText("Buffer Debug Mode: " + current.name()), true);
		}

		while (CanvasMod.DECREMENT_A.wasPressed()) {
			Configurator.bloomIntensity = Math.max(0, Configurator.bloomIntensity - 0.01f);
			MinecraftClient.getInstance().player.sendMessage(new LiteralText("Bloom Intensity = " + Configurator.bloomIntensity), true);
		}

		while (CanvasMod.INCREMENT_A.wasPressed()) {
			Configurator.bloomIntensity += 0.01f;
			MinecraftClient.getInstance().player.sendMessage(new LiteralText("Bloom Intensity = " + Configurator.bloomIntensity), true);
		}

		while (CanvasMod.DECREMENT_B.wasPressed()) {
			Configurator.bloomScale = Math.max(0, Configurator.bloomScale - 0.05f);
			MinecraftClient.getInstance().player.sendMessage(new LiteralText("Bloom Scale = " + Configurator.bloomScale), true);
		}

		while (CanvasMod.INCREMENT_B.wasPressed()) {
			Configurator.bloomScale += 0.05f;
			MinecraftClient.getInstance().player.sendMessage(new LiteralText("Bloom Scale = " + Configurator.bloomScale), true);
		}

		current.task.run();
	}
}