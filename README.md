copycore
========

This is a library mod which is required for some of my other mods such as [Advanced
Health Options](https://github.com/copygirl/AdvHealthOptions). It provides many utility
classes and methods which can be used by multiple different mods, mainly to ease
development and to make pretty code. It doesn't come with many functions on its own,
though that might change in the future.

### Features ###

Note that all tweaks are disabled by default and have to be enabled in the config.

- **Auto replace**  
  Pulls items from the column above when stack is used up or tool breaks.
  This is currently a server-side only tweak.

- **Double door interaction**  
  Interacting with a wooden door opens both doors at once.

- **Custom item despawn timer on player death**  
  Allows changing how long items stick around when dropped by a dying player.

- **[MineTweaker](http://minetweaker3.powerofbytes.com/) methods**  
  Adds the following methods for use in MineTweaker scrips:

```
mods.copycore.Tweaks.setDurability(item, durability)
mods.copycore.Tweaks.setDurability([items], durability)
mods.copycore.Tweaks.setArmorDurability(head, chest, legs, boots, durabilityFactor)
mods.copycore.Tweaks.setMaxStackSize(item, stackSize)
mods.copycore.Tweaks.setMaxStackSize([items], stackSize)
```

### Download ###

All downloads are available through the
[releases page](https://github.com/copygirl/copycore/releases).
