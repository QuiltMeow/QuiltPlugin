// Transplant From Quilt Maple Story 117
package ew.quilt.script;

import ew.quilt.Config.ConfigManager;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class JSExecuteLibrary implements Serializable {

    private static final long serialVersionUID = 7311670219067447109L;
    private static final JSExecuteLibrary INSTANCE = new JSExecuteLibrary();

    public static void listEngine(CommandSender sender) {
        ScriptEngineManager manager = new ScriptEngineManager();
        List<ScriptEngineFactory> factories = manager.getEngineFactories();
        for (ScriptEngineFactory factory : factories) {
            sender.sendMessage("腳本引擎名稱 : " + factory.getEngineName());
            sender.sendMessage("腳本引擎版本 : " + factory.getEngineVersion());
            sender.sendMessage("語言名稱 : " + factory.getLanguageName());
            sender.sendMessage("語言版本 : " + factory.getLanguageVersion());
            sender.sendMessage("相關名稱 : " + factory.getNames());
            sender.sendMessage("MIME 類型 : " + factory.getMimeTypes());
            sender.sendMessage("擴展 : " + factory.getExtensions());
            sender.sendMessage("－－－－－－－－－－");
        }
    }

    private Map<String, CompiledScript> compiled = new HashMap<>();

    public static JSExecuteLibrary getInstance() {
        return INSTANCE;
    }

    public void putBaseVariable(ScriptEngine engine, CommandSender sender) {
        engine.put("jm", new JSExport());
        engine.put("sender", sender);
    }

    public boolean executeScript(String command) {
        return executeScript(ConfigManager.getConsoleCommandSender(), command);
    }

    public boolean executeScript(CommandSender sender, String command) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("js");
            putBaseVariable(engine, sender);
            engine.eval(command);
            return true;
        } catch (ScriptException ex) {
            sender.sendMessage(ChatColor.RED + "腳本指令執行失敗 ...");
        }
        return false;
    }

    public boolean executeJSFile(String name) {
        return executeJSFile(ConfigManager.getConsoleCommandSender(), name);
    }

    public boolean executeJSFile(CommandSender sender, String name) {
        try (FileReader reader = new FileReader("plugins/scripts/" + name)) {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("js");
            putBaseVariable(engine, sender);
            engine.eval(reader);
            return true;
        } catch (IOException ex) {
            sender.sendMessage(ChatColor.RED + "腳本指令執行發生錯誤 檔案讀取失敗 : " + ex);
        } catch (ScriptException ex) {
            sender.sendMessage(ChatColor.RED + "腳本指令執行發生錯誤 指令執行失敗 : " + ex);
        }
        return false;
    }

    public boolean compile(String name, String command) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        putBaseVariable(engine, ConfigManager.getConsoleCommandSender());
        try {
            Compilable compileEngine = (Compilable) engine;
            CompiledScript script = compileEngine.compile(command);
            compiled.put(name, script);
            return true;
        } catch (ScriptException ex) {
            ConfigManager.getConsoleCommandSender().sendMessage(ChatColor.RED + "編譯腳本時發生錯誤 : " + ex);
        }
        return false;
    }

    public boolean compileFile(String name, String file) {
        try (FileReader reader = new FileReader("plugins/scripts/" + file)) {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("js");
            putBaseVariable(engine, ConfigManager.getConsoleCommandSender());
            Compilable compileEngine = (Compilable) engine;
            CompiledScript script = compileEngine.compile(reader);
            compiled.put(name, script);
            return true;
        } catch (IOException ex) {
            ConfigManager.getConsoleCommandSender().sendMessage(ChatColor.RED + "編譯腳本時發生錯誤 檔案讀取失敗 : " + ex);
        } catch (ScriptException ex) {
            ConfigManager.getConsoleCommandSender().sendMessage(ChatColor.RED + "編譯腳本時發生錯誤 指令執行失敗 : " + ex);
        }
        return false;
    }

    public boolean executeCompiled(String name) {
        CommandSender sender = ConfigManager.getConsoleCommandSender();
        CompiledScript script = compiled.get(name);
        if (script == null) {
            sender.sendMessage(ChatColor.RED + "找不到指令腳本封裝");
            return false;
        }
        try {
            script.eval();
            sender.sendMessage(ChatColor.GREEN + "指令腳本已執行");
            return true;
        } catch (ScriptException ex) {
            sender.sendMessage(ChatColor.RED + "執行已編譯腳本時發生錯誤 : " + ex);
        }
        return false;
    }

    public Map<String, CompiledScript> getCompiled() {
        return compiled;
    }

    public void setCompiled(Map<String, CompiledScript> set) {
        compiled = set;
    }

    public void removeCompiled(String name) {
        compiled.remove(name);
    }
}
