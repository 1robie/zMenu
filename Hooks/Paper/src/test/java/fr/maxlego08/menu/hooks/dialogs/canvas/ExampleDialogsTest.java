package fr.maxlego08.menu.hooks.dialogs.canvas;

import com.google.gson.JsonArray;
import fr.maxlego08.menu.api.ButtonManager;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.MenuPlugin;
import fr.maxlego08.menu.api.button.buttons.dialogs.body.DialogCanvasBody;
import fr.maxlego08.menu.api.dialog.canvas.*;
import fr.maxlego08.menu.api.requirement.Requirement;
import fr.maxlego08.menu.hooks.dialogs.canvas.check.CanvasChecker;
import fr.maxlego08.menu.hooks.dialogs.canvas.element.*;
import fr.maxlego08.menu.hooks.dialogs.canvas.glyph.PanelGlyphProvider;
import fr.maxlego08.menu.hooks.dialogs.canvas.glyph.ProgressGlyphProvider;
import fr.maxlego08.menu.hooks.dialogs.canvas.glyph.ZMenuGlyphProvider;
import fr.maxlego08.menu.hooks.dialogs.canvas.layout.LayoutEngine;
import fr.maxlego08.menu.hooks.dialogs.canvas.text.CanvasTextFont;
import fr.maxlego08.menu.hooks.dialogs.canvas.widget.WidgetLoaders;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Loads the canvases of the dialogs zMenu ships, with the real element loaders, and checks them the way the
 * server does when the dialogs load: the examples must have no problem, except the mistakes made on purpose.
 */
public class ExampleDialogsTest {

    private static final Path RESOURCES = Path.of("..", "..", "src", "main", "resources");
    private static final GlyphCell VANILLA_CELL = new GlyphCell(Key.key("minecraft", "item/x.png"), 1, 1, 0, 0, 16, 16, 16, 16, null, null, 0);

    private static final CanvasTextFont FONT = new CanvasTextFont() {
        @Override
        public @NotNull String name() {
            return "default";
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public double advance(int codepoint) {
            return codepoint == ' ' ? 4 : 6;
        }

        @Override
        public int lineHeight() {
            return 9;
        }

        @Override
        public @NotNull JsonArray providers(int y) {
            return new JsonArray();
        }

        @Override
        public @NotNull SortedMap<String, byte[]> files() {
            return new TreeMap<>();
        }
    };

    @BeforeAll
    static void logger() {
        CanvasTestSupport.installLogger();
    }

    private interface Answer extends Function<Object, Object> {
    }

    private static <T> T stub(Class<T> type, Map<String, Object> answers) {
        Object proxy = Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, (instance, method, arguments) -> {
            if (answers.containsKey(method.getName())) {
                Object answer = answers.get(method.getName());
                return answer instanceof Answer function && arguments != null ? function.apply(arguments[0]) : answer;
            }
            Class<?> returned = method.getReturnType();
            if (returned == boolean.class) return false;
            if (returned == int.class || returned == long.class || returned == double.class || returned == float.class) return 0;
            if (returned == List.class) return List.of();
            if (returned == Optional.class) return Optional.empty();
            return null;
        });
        return type.cast(proxy);
    }

    private static MenuPlugin plugin() {
        Map<String, CanvasElementLoader> loaders = new HashMap<>();
        for (CanvasElementLoader loader : List.of(new ImageElementLoader(), new TextElementLoader(), new ButtonElementLoader(), new PanelElementLoader(),
                new ProgressElementLoader(), new LayoutElementLoader(LayoutEngine.Direction.ROW), new LayoutElementLoader(LayoutEngine.Direction.COLUMN),
                new ObjectElementLoaders.Head(), new ObjectElementLoaders.Sprite(), new ItemElementLoader())) {
            loaders.put(loader.key().value(), loader);
        }
        for (CanvasElementLoader loader : WidgetLoaders.loaders()) loaders.put(loader.key().value(), loader);
        Answer elementLoader = type -> Optional.ofNullable(loaders.get(type.toString()));
        CanvasManager canvasManager = stub(CanvasManager.class, Map.of("getElementLoader", elementLoader));
        Requirement requirement = stub(Requirement.class, Map.of());
        ButtonManager buttonManager = stub(ButtonManager.class, Map.of("loadRequirement", requirement));
        InventoryManager inventoryManager = stub(InventoryManager.class, Map.of("getRegistries", DialogActionRegistries.create()));
        return stub(MenuPlugin.class, Map.of("getCanvasManager", canvasManager, "getButtonManager", buttonManager, "getInventoryManager", inventoryManager));
    }

    private static CanvasVariants request() {
        Path pack = RESOURCES.resolve("resource-pack");
        Map<String, GlyphProvider> providers = Map.of("zmenu", new ZMenuGlyphProvider(pack), "panel", new PanelGlyphProvider(pack),
                "progress", new ProgressGlyphProvider(pack));
        return new CanvasVariants() {
            @Override
            public void glyph(@NotNull ResolvedGlyph glyph, int y) {
            }

            @Override
            public void text(@NotNull String font, int y) {
            }

            @Override
            public void area(int y, int width, int height) {
            }

            @Override
            public @Nullable ResolvedGlyph resolve(@NotNull String reference) {
                int separator = reference.indexOf(':');
                String provider = separator < 0 ? "zmenu" : reference.substring(0, separator);
                String path = separator < 0 ? reference : reference.substring(separator + 1);
                if (provider.equals("minecraft")) return new ResolvedGlyph(reference, List.of(VANILLA_CELL), false);
                GlyphProvider glyphProvider = providers.get(provider);
                return glyphProvider == null ? null : glyphProvider.resolve(path).orElse(null);
            }
        };
    }

    private static Map<String, List<String>> check(Path file) throws Exception {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file.toFile());
        String dialog = file.getFileName().toString().replace(".yml", "");
        CanvasChecker checker = new CanvasChecker(request(), name -> name.equals("default") ? FONT : null, MiniMessage.miniMessage()::deserialize);
        MenuPlugin plugin = plugin();
        ConfigurationSection bodies = configuration.getConfigurationSection("body");
        if (bodies == null) return Map.of();
        for (String bodyName : bodies.getKeys(false)) {
            ConfigurationSection body = bodies.getConfigurationSection(bodyName);
            if (body == null || !"dialog-canvas".equals(body.getString("type"))) continue;
            String where = file.getFileName() + " " + bodyName;
            List<CanvasElement> elements = new ArrayList<>();
            ConfigurationSection elementsSection = body.getConfigurationSection("elements");
            for (String name : elementsSection.getKeys(false)) {
                ConfigurationSection section = elementsSection.getConfigurationSection(name);
                CanvasElementLoader loader = plugin.getCanvasManager().getElementLoader(section.getString("type")).orElseThrow();
                elements.add(ConditionalElement.load(plugin, loader, name, section, file.toFile().getPath()));
            }
            checker.check(dialog, new DialogCanvasBody(CanvasValue.number(body, "width", 200, 1, 1014, where), CanvasValue.number(body, "height", 90, 1, 4096, where),
                    elements, body.getStringList("fallback")));
        }
        return checker.problems();
    }

    private static List<Path> canvasDialogs() throws Exception {
        try (Stream<Path> files = Files.walk(RESOURCES.resolve("dialogs"))) {
            return files.filter(path -> path.toString().endsWith(".yml"))
                    .filter(path -> {
                        try {
                            return Files.readString(path).contains("dialog-canvas");
                        } catch (IOException exception) {
                            throw new UncheckedIOException(exception);
                        }
                    })
                    .toList();
        }
    }

    @Test
    public void theExampleCanvasesLoadWithoutProblems() throws Exception {
        List<Path> dialogs = canvasDialogs();
        assertTrue(dialogs.size() >= 7, dialogs.toString());
        for (Path dialog : dialogs) {
            if (dialog.getFileName().toString().equals("canvas-checks.yml")) continue;
            assertEquals(Map.of(), check(dialog), dialog.toString());
        }
    }

    @Test
    public void theMistakesExampleShowsEveryWarning() throws Exception {
        List<String> problems = check(RESOURCES.resolve("dialogs").resolve("canvas-tests").resolve("canvas-checks.yml")).get("canvas-checks");

        List<String> elements = problems.stream().map(problem -> problem.substring(0, problem.indexOf(':'))).toList();
        assertEquals(List.of("missing", "unknown_font", "too_low", "too_wide", "too_long", "too_many"), elements, String.join("\n", problems));
    }

    @Test
    public void theExampleFilesAreNotInstalledByDefault() throws Exception {
        String plugin = Files.readString(Path.of("..", "..", "src", "main", "java", "fr", "maxlego08", "menu", "ZMenuPlugin.java"));
        for (Path dialog : canvasDialogs()) {
            String relative = RESOURCES.relativize(dialog).toString().replace(File.separatorChar, '/');
            assertFalse(plugin.contains("\"" + relative + "\""), relative + " is in the files zMenu installs, so every server would generate a canvas pack");
        }
    }
}
