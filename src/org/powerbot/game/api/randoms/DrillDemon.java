package org.powerbot.game.api.randoms;

import java.util.HashMap;

import org.powerbot.game.api.AntiRandom;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Npcs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.Locations;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.Npc;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.node.Location;

@Manifest(name = "Drill Demon", description = "Exercises.", version = 1.0, authors = {"Timer"})
public class DrillDemon extends AntiRandom {
	private static final Area AREA = new Area(new Tile(3159, 4818, 0), new Tile(3167, 4822, 0));
	private static final int NPC_ID_DEMON = 2790;
	private static final int WIDGET_MAT = 1190;
	private static final int WIDGET_MAT_ICON = 1;

	private final static HashMap<Integer, Integer> mat_indices = new HashMap<Integer, Integer>();
	private final static HashMap<Integer, int[]> setting_arrays = new HashMap<Integer, int[]>();

	static {
		mat_indices.put(10949, 0); //Star jumps
		mat_indices.put(10946, 1); //Push ups
		mat_indices.put(10948, 2); //Sit ups
		mat_indices.put(10947, 3); //Jog

		setting_arrays.put(668, new int[]{0, 1, 2, 3});
		setting_arrays.put(675, new int[]{1, 0, 2, 3});
		setting_arrays.put(724, new int[]{0, 2, 1, 3});
		setting_arrays.put(738, new int[]{2, 0, 1, 3});
		setting_arrays.put(787, new int[]{1, 2, 0, 3});
		setting_arrays.put(794, new int[]{2, 1, 0, 3});
		setting_arrays.put(1116, new int[]{0, 1, 3, 2});
		setting_arrays.put(1123, new int[]{1, 0, 3, 2});
		setting_arrays.put(1228, new int[]{0, 3, 1, 2});
		setting_arrays.put(1249, new int[]{3, 0, 1, 2});
		setting_arrays.put(1291, new int[]{1, 3, 0, 2});
		setting_arrays.put(1305, new int[]{3, 1, 0, 2});
		setting_arrays.put(1620, new int[]{0, 2, 3, 1});
		setting_arrays.put(1634, new int[]{2, 0, 3, 1});
		setting_arrays.put(1676, new int[]{0, 3, 2, 1});
		setting_arrays.put(1697, new int[]{3, 0, 2, 1});
		setting_arrays.put(1802, new int[]{2, 3, 0, 1});
		setting_arrays.put(1809, new int[]{3, 2, 0, 1});
		setting_arrays.put(2131, new int[]{1, 2, 3, 0});
		setting_arrays.put(2138, new int[]{2, 1, 3, 0});
		setting_arrays.put(2187, new int[]{1, 3, 2, 0});
		setting_arrays.put(2201, new int[]{3, 1, 2, 0});
		setting_arrays.put(2250, new int[]{2, 3, 1, 0});
		setting_arrays.put(2257, new int[]{3, 2, 1, 0});
	}

	@Override
	public boolean validate() {
		return Game.isLoggedIn() && AREA.contains(Players.getLocal().getPosition());
	}

	@Override
	public void run() {
		if (Camera.getPitch() < 90) {
			Camera.setPitch(true);
		}
		Camera.setAngle('n');
		final Player localPlayer = Players.getLocal();

		if (localPlayer.isMoving()) {
			for (int i = 0; i < 50; i++) {
				if (!localPlayer.isMoving()) {
					break;
				}
				Time.sleep(Random.nextInt(75, 80));
			}
			Time.sleep(Random.nextInt(1800, 2000));
			return;
		}

		if (localPlayer.getAnimation() != -1) {
			for (int i = 0; i < 50; i++) {
				if (localPlayer.getAnimation() == -1) {
					break;
				}
				Time.sleep(Random.nextInt(60, 90));
			}
			for (int i = 0; i < 50; i++) {
				if (Widgets.get(241, 0).validate()) {
					break;
				}
				Time.sleep(Random.nextInt(30, 40));
			}
			Time.sleep(Random.nextInt(400, 700));
			return;
		}

		if (Widgets.get(WIDGET_MAT).validate()) {
			final int setting_value = Settings.get(Settings.VALUE_RANDOMEVENT_DRILLDEMON_MAT);
			final int child_id = Widgets.get(WIDGET_MAT, WIDGET_MAT_ICON).getChildId();
			for (int i = 0; i < setting_arrays.get(setting_value).length; i++) {
				if (setting_arrays.get(setting_value)[i] == mat_indices.get(child_id)) {
					if (findAndUseMat(i)) {
						Time.sleep(800);
						return;
					}
				}
			}
		}

		if (Widgets.clickContinue()) {
			Time.sleep(Random.nextInt(2000, 3000));
			return;
		}

		if (!Widgets.clickContinue() && localPlayer.getAnimation() == -1) {
			final Npc demon = Npcs.getNearest(NPC_ID_DEMON);
			demon.interact("Talk-to");
		}
		Time.sleep(Random.nextInt(2000, 2500));
	}

	private boolean findAndUseMat(final int sign_id) {
		final Location[] game_mats = {
				Locations.getNearest(10076),
				Locations.getNearest(10077),
				Locations.getNearest(10078),
				Locations.getNearest(10079)
		};
		if (game_mats[sign_id] != null) {
			if (!game_mats[sign_id].isOnScreen()) {
				if (Walking.walk(game_mats[sign_id].getPosition())) {
					Time.sleep(500);
				}
			} else {
				if (Players.getLocal().getAnimation() == -1) {
					if (game_mats[sign_id].interact("Use")) {
						Time.sleep(900);
						return true;
					}
				}
			}
		}
		return false;
	}
}
