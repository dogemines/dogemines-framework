# DogeMines Framework
Part of an old project of mine where I tried to make a vanilla Minecraft server with custom content. Archived; no longer updated.

---

A server development framework for use with PaperMC servers, which is developed for and used in the creation of my Minecraft server. It features creation of many custom objects, all done through a server-sided plugin without any required mods on the client.

### Features
**Resource pack**\
The library features creation of various custom objects that can have custom textures or be interacted with in different ways
- Custom textured item and tool creation.
- Custom textured block and tile entity creation.
- Custom sound event creation

**Menus**\
Menus can be shown to a player and can have callbacks that run when the player selects a certain option.
- Menu implementation using vanilla inventories and click callbacks for slots. Supports groups of multiple inventories (pages), which are created through an intuitive builder.
