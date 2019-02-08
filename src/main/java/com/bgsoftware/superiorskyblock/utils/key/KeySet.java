package com.bgsoftware.superiorskyblock.utils.key;

import com.bgsoftware.superiorskyblock.api.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class KeySet extends AbstractSet<Key> implements Set<Key> {

    private Set<String> set;

    public KeySet(List<String> keys){
        this.set = new HashSet<>(keys);
    }

    @Override
    public Iterator<Key> iterator() {
        return asKeySet().iterator();
    }

    @Override
    public int size() {
        return set.size();
    }

    public boolean contains(ItemStack itemStack) {
        return contains(SKey.of(itemStack));
    }

    public boolean contains(Material material, short data) {
        return contains(SKey.of(material, data));
    }

    public boolean contains(String key) {
        return contains(SKey.of(key));
    }

    @Override
    public boolean contains(Object o) {
        if(o instanceof SKey){
            String key = o.toString();
            if(set.contains(key))
                return true;
            else if(key.contains(":") && set.contains(key.split(":")[0]))
                return true;
            else if(key.contains(";") && set.contains(key.split(";")[0]))
                return true;
        }
        return super.contains(o);
    }

    @Override
    public boolean add(Key key) {
        return set.add(key.toString());
    }

    @Override
    public boolean remove(Object o) {
        return set.remove(o);
    }

    private Set<Key> asKeySet(){
        Set<Key> set = new HashSet<>();
        this.set.forEach(string -> set.add(SKey.of(string)));
        return set;
    }

}