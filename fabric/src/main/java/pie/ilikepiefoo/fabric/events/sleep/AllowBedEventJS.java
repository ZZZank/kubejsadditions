package pie.ilikepiefoo.fabric.events.sleep;

import dev.latvian.mods.kubejs.entity.LivingEntityEventJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.server.ServerScriptManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import pie.ilikepiefoo.fabric.FabricEventsJS;

/**
 * An event that is called to check whether a block is valid for sleeping.
 *
 * <p>Used for checking whether the block at the current sleeping position is a valid bed block.
 * If {@code false}, the player wakes up.
 *
 * <p>This event is only checked <i>during</i> sleeping, so an entity can
 * {@linkplain LivingEntity#startSleeping(BlockPos) start sleeping} on any block, but will immediately
 * wake up if this check fails.
 *
 * @see LivingEntity#checkBedExists()
 */
public class AllowBedEventJS extends LivingEntityEventJS {
	private final LivingEntity entity;
	private final BlockPos sleepingPos;
	private final BlockState state;
	private final boolean vanillaResult;

	public AllowBedEventJS( LivingEntity entity, BlockPos sleepingPos, BlockState state, boolean vanillaResult ) {
		this.entity = entity;
		this.sleepingPos = sleepingPos;
		this.state = state;
		this.vanillaResult = vanillaResult;
	}

	/**
	 * Checks whether a block is a valid bed for the entity.
	 *
	 * <p>Non-{@linkplain InteractionResult#PASS passing} return values cancel further callbacks.
	 *
	 * @param entity        the sleeping entity
	 * @param sleepingPos   the position of the block
	 * @param state         the block state to check
	 * @param vanillaResult {@code true} if vanilla allows the block, {@code false} otherwise
	 * @return {@link InteractionResult#SUCCESS} if the bed is valid, {@link InteractionResult#FAIL} if it's not,
	 * {@link InteractionResult#PASS} to fall back to other callbacks
	 */
	public static InteractionResult handler( LivingEntity entity, BlockPos sleepingPos, BlockState state, boolean vanillaResult ) {
		if (ServerScriptManager.instance == null || !FabricEventsJS.ALLOW_BED.hasListeners()) {
			return InteractionResult.PASS;
		}

		return FabricEventsJS.ALLOW_BED.post(new AllowBedEventJS(entity, sleepingPos, state, vanillaResult)).arch().asMinecraft();
	}

	public LivingEntity getEntity() {
		return entity;
	}

	public BlockContainerJS getPos() {
		return getLevel().kjs$getBlock(sleepingPos);
	}

	public BlockPos getSleepingPos() {
		return sleepingPos;
	}

	public BlockState getState() {
		return state;
	}

	public boolean isVanillaResult() {
		return vanillaResult;
	}

}

