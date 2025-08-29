package net.dogemines.framework;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.dogemines.framework.data.registry.Registrable;
import net.dogemines.framework.data.registry.Registries;
import net.dogemines.framework.data.registry.RegistryObject;
import net.dogemines.framework.item.CustomItem;
import net.dogemines.framework.item.CustomItemStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

final class RegistryCommands {
    public static void registerCommands(Commands registrar) {
        LiteralCommandNode<CommandSourceStack> dogeminesCommand = Commands.literal("dogemines")

                .then(Commands.literal("give")
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .then(Commands.argument("item", new RegistryPairArgumentType<>(Registries.ITEM))
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1, 64))
                                                .executes(ctx -> {
                                                    final PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                                                    final Player target = targetResolver.resolve(ctx.getSource()).getFirst();

                                                    @SuppressWarnings("unchecked")
                                                    final RegistryObject<CustomItem> itemPair = ctx.getArgument("item", RegistryObject.class);
                                                    final int amount = ctx.getArgument("amount", Integer.class);

                                                    target.give(CustomItemStack.of(itemPair, amount));

                                                    CommandSender sender = ctx.getSource().getSender();
                                                    sender.sendMessage(Component.text(String.format("Gave %d %s to %s", amount, itemPair.getId(), target.getName()), NamedTextColor.GREEN));

                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                )
                        )
                )

                /*.then(Commands.literal("inventory")
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .then(Commands.argument("inventory", new RegistryPairArgumentType<>(Registries.MENU))
                                        .executes(ctx -> {
                                            final PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                                            final Player target = targetResolver.resolve(ctx.getSource()).getFirst();

                                            @SuppressWarnings("unchecked")
                                            final RegistryPair<InventoryMenu> menuPair = ctx.getArgument("inventory", RegistryPair.class);

                                            menuPair.getValue().openInventory(target);

                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )
                )*/

                //------------------------------------------
                // Debug commands
                //------------------------------------------
               /* .then(Commands.literal(("debug"))
                        .then(Commands.literal("summonnpc")
                                .then(Commands.argument("location", ArgumentTypes.finePosition(true))
                                        .then(Commands.argument("skin", new RegistryArgumentType<>(PlayerSkin.class))
                                                .then(Commands.argument("name", StringArgumentType.greedyString())
                                                        .executes(ctx -> {
                                                            final FinePositionResolver resolver = ctx.getArgument("location", FinePositionResolver.class);
                                                            final FinePosition finePosition = resolver.resolve(ctx.getSource());

                                                            final String name = ctx.getArgument("name", String.class);
                                                            final PlayerSkin skin = ctx.getArgument("skin", PlayerSkin.class);

                                                            PlayerNPC.create(finePosition.toLocation(DogeMines.WORLD), name, skin);

                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                )
                                        )
                                )
                        )

                )*/

                .build();

        registrar.register(dogeminesCommand);
    }
}

class RegistryPairArgumentType<T extends Registrable> implements CustomArgumentType.Converted<@NotNull RegistryObject<T>, @NotNull String> {

    private final Registries<T> registry;

    public RegistryPairArgumentType(Registries<T> registry) {
        this.registry = registry;
    }

    @Override
    public @NotNull RegistryObject<T> convert(String nativeType) throws CommandSyntaxException {
        for (RegistryObject<T> pair : registry.getPairs()) {
            if (pair.getId().equalsIgnoreCase(nativeType)) {
                return pair;
            }
        }
        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect()
                .create(registry.getClass().getName());
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for (RegistryObject<T> pair : registry.getPairs()) {
            String name = pair.getId();
            if (name.startsWith(builder.getRemaining().toLowerCase())) {
                builder.suggest(name);
            }
        }
        return builder.buildFuture();
    }
}

