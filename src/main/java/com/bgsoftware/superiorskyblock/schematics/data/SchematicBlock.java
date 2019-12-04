package com.bgsoftware.superiorskyblock.schematics.data;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.utils.blocks.BlockChangeTask;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.SkullType;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.block.banner.Pattern;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class SchematicBlock {

    private static final SuperiorSkyblockPlugin plugin = SuperiorSkyblockPlugin.getPlugin();

    public final static SchematicBlock AIR = of(0);

    private final int combinedId;

    private SchematicBlock(int combinedId){
        this.combinedId = combinedId;
    }

    public int getCombinedId() {
        return combinedId;
    }

    public void applyBlock(BlockChangeTask blockChangeTask, Location location, Island island){
        applyBlock(blockChangeTask, location, (Runnable) null);
    }

    protected void applyBlock(BlockChangeTask blockChangeTask, Location location, Runnable onFinish){
        blockChangeTask.setBlock(location, combinedId, onFinish);
    }

    private static class SchematicBanner extends SchematicBlock{

        private final DyeColor baseColor;
        private final List<Pattern> patterns;

        private SchematicBanner(int combinedId, DyeColor baseColor, List<Pattern> patterns){
            super(combinedId);
            this.baseColor = baseColor;
            this.patterns = patterns;
        }

        @Override
        public void applyBlock(BlockChangeTask blockChangeTask, Location location, Island island) {
            super.applyBlock(blockChangeTask, location, () -> {
                Banner banner = (Banner) location.getBlock().getState();

                if(baseColor != null)
                    banner.setBaseColor(baseColor);

                if(!patterns.isEmpty())
                    banner.setPatterns(patterns);

                banner.update();
            });
        }
    }

    private static class SchematicInventoryHolder extends SchematicBlock{

        private final ItemStack[] contents;

        private SchematicInventoryHolder(int combinedId, ItemStack[] contents){
            super(combinedId);
            this.contents = contents;
        }

        @Override
        public void applyBlock(BlockChangeTask blockChangeTask, Location location, Island island) {
            super.applyBlock(blockChangeTask, location, () -> {
                Inventory inventory = ((InventoryHolder) location.getBlock().getState()).getInventory();

                if(plugin.getSettings().starterChestEnabled && inventory.getType() == InventoryType.CHEST){
                    for(Map.Entry<Integer, ItemStack> entry : plugin.getSettings().starterChestContents.entrySet()){
                        inventory.setItem(entry.getKey(), entry.getValue());
                    }
                }
                else {
                    inventory.setContents(contents);
                }
            });
        }
    }

    private static class SchematicFlowerPot extends SchematicBlock{

        private final ItemStack flower;

        private SchematicFlowerPot(int combinedId, ItemStack flower){
            super(combinedId);
            this.flower = flower;
        }

        @Override
        public void applyBlock(BlockChangeTask blockChangeTask, Location location, Island island) {
            super.applyBlock(blockChangeTask, location, () ->
                    plugin.getNMSAdapter().setFlowerPot(location, flower));
        }
    }

    private static class SchematicSkull extends SchematicBlock{

        private final SkullType skullType;
        private final BlockFace rotation;
        private final String owner;

        private SchematicSkull(int combinedId, SkullType skullType, BlockFace rotation, String owner){
            super(combinedId);
            this.skullType = skullType;
            this.rotation = rotation;
            this.owner = owner;
        }

        @Override
        public void applyBlock(BlockChangeTask blockChangeTask, Location location, Island island) {
            super.applyBlock(blockChangeTask, location, () -> {
                Skull skull = (Skull) location.getBlock().getState();

                if(skullType != null) {
                    try {
                        skull.setSkullType(skullType);
                    } catch (UnsupportedOperationException ignored) {
                    }
                }

                if(rotation != null)
                    skull.setRotation(rotation);

                if(!owner.isEmpty())
                    skull.setOwner(owner);

                skull.update();
            });
        }
    }

    private static class SchematicSign extends SchematicBlock{

        private final String[] lines;

        private SchematicSign(int combinedId, String[] lines){
            super(combinedId);
            this.lines = lines;
        }

        @Override
        public void applyBlock(BlockChangeTask blockChangeTask, Location location, Island island) {
            super.applyBlock(blockChangeTask, location, () -> {
                Sign sign = (Sign) location.getBlock().getState();
                for(int i = 0; i < 4; i++) {
                    sign.setLine(i, lines[i]
                            .replace("{player}", island == null ? "" : island.getOwner().getName())
                            .replace("{island}", island == null ? "" : island.getName()));
                }
                sign.update();
            });
        }
    }

    public static SchematicBlock of(int combinedId){
        return new SchematicBlock(combinedId);
    }

    public static SchematicBanner of(int combinedId, DyeColor baseColor, List<Pattern> patterns){
        return new SchematicBanner(combinedId, baseColor, patterns);
    }

    public static SchematicInventoryHolder of(int combinedId, ItemStack[] contents){
        return new SchematicInventoryHolder(combinedId, contents);
    }

    public static SchematicFlowerPot of(int combinedId, ItemStack flower){
        return new SchematicFlowerPot(combinedId, flower);
    }

    public static SchematicSkull of(int combinedId, SkullType skullType, BlockFace rotation, String owner){
        return new SchematicSkull(combinedId, skullType, rotation, owner);
    }

    public static SchematicSign of(int combinedId, String[] lines){
        return new SchematicSign(combinedId, lines);
    }

}