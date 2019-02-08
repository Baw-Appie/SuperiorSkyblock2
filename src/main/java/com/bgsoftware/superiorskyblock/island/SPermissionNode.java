package com.bgsoftware.superiorskyblock.island;

import com.bgsoftware.superiorskyblock.Locale;
import com.bgsoftware.superiorskyblock.api.island.IslandPermission;
import com.bgsoftware.superiorskyblock.api.island.PermissionNode;
import com.bgsoftware.superiorskyblock.utils.jnbt.ListTag;
import com.bgsoftware.superiorskyblock.utils.jnbt.StringTag;
import com.bgsoftware.superiorskyblock.utils.jnbt.Tag;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public final class SPermissionNode implements PermissionNode {

    private Set<IslandPermission> nodes = new HashSet<>();

    public SPermissionNode(ListTag tag){
        List<Tag> list = tag.getValue();

        for(Tag _tag : list)
            nodes.add(IslandPermission.valueOf(((StringTag) _tag).getValue()));

    }

    public SPermissionNode(SPermissionNode other, List<String> permissions){
        if(other != null) {
            this.nodes = new HashSet<>(other.nodes);
        }

        List<IslandPermission> permissionList = new ArrayList<>();

        for(String permission : permissions)
            permissionList.add(IslandPermission.valueOf(permission));

        nodes.addAll(permissionList);
    }

    public boolean hasPermission(IslandPermission permission){
        return nodes.contains(IslandPermission.ALL) || nodes.contains(permission);
    }

    public void setPermission(IslandPermission permission, boolean value){
        if(value){
            nodes.add(permission);
        }else{
            nodes.remove(permission);
        }
    }

    public String getColoredPermissions(){
        StringBuilder stringBuilder = new StringBuilder();

        for(IslandPermission islandPermission : IslandPermission.values()){
            boolean isEnabled = hasPermission(islandPermission);
            stringBuilder.append(Locale.PERMISSION_SPACER.getMessage()).append(isEnabled ? "&a" : "&c").append(islandPermission.name().toLowerCase());
        }

        //noinspection ConstantConditions
        int length = Locale.PERMISSION_SPACER.getMessage() == null ? 0 : Locale.PERMISSION_SPACER.getMessage().length();

        String message = stringBuilder.toString().substring(length);

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public SPermissionNode clone() {
        try {
            SPermissionNode permissionNode = (SPermissionNode) super.clone();
            permissionNode.nodes = new HashSet<>(nodes);
            return permissionNode;
        }catch(CloneNotSupportedException ex){
            return new SPermissionNode(this, new ArrayList<>());
        }
    }

    public Tag getAsTag(){
        List<Tag> enabledPermissions = new ArrayList<>();

        nodes.forEach(islandPermission -> enabledPermissions.add(new StringTag(islandPermission.name())));

        return new ListTag(StringTag.class, enabledPermissions);
    }


}