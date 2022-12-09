package net.minecraftforge.gradle.json.version;

import com.google.gson.internal.LinkedTreeMap;

import java.util.*;

public class Version {
    public String id;
    public Date time;
    public Date releaseTime;
    public String type;
    public String minecraftArguments;
    public String inheritsFrom;
    private List<Library> libraries;
    public String mainClass;
    public int minimumLauncherVersion;
    public String incompatibilityReason;
    public AssetIndexRef assetIndex;
    private Map<String, Download> downloads;
    public List<OSRule> rules;
    private List<Library> _libraries;

    public List<Library> getLibraries() {
        if (_libraries == null) {
            _libraries = new ArrayList<Library>();
            if (libraries == null) return _libraries;
            for (Library lib : libraries) {
                if (lib.applies()) {
                    _libraries.add(lib);
                }
            }
        }
        return _libraries;
    }
    public String getAssets() {
        return assetIndex.id;
    }
    public String getClientUrl()
    {
        return downloads.get("client").url;
    }

    public String getServerUrl()
    {
        return downloads.get("server").url;
    }
    /**
     * Populates this instance with information from another version json.
     *
     * @param version Version json to extend from
     */
    public void extendFrom(Version version) {
        // strings. replace if null.
        if (minecraftArguments == null)
            minecraftArguments = version.minecraftArguments;
        if (mainClass == null)
            mainClass = version.mainClass;
        if (incompatibilityReason == null)
            incompatibilityReason = version.incompatibilityReason;
        if (assetIndex == null)
            assetIndex = version.assetIndex;
        if (downloads == null)
            downloads = version.downloads;

        // lists.  replace if null, add if not.
        if (libraries == null)
            libraries = version.libraries;
        else if (version.libraries != null)
            libraries.addAll(0, version.libraries);

        if (rules == null)
            rules = version.rules;
        else if (version.rules != null)
            rules.addAll(0, version.rules);
    }

    public void setDownloads(Map<String, Download> downloads) {
        this.downloads = downloads;
    }

    public Map<String, Download> getDownloads() {
        return this.downloads;
    }

    public static AssetIndexRef dodgyAssetIndexPatch = new AssetIndexRef("1.7.10",
            "1863782e33ce7b584fc45b037325a1964e095d3e",
            "https://launchermeta.mojang.com/v1/packages/1863782e33ce7b584fc45b037325a1964e095d3e/1.7.10.json",
            72996,
            112396854);
    public static Download client = new Download("e80d9b3bf5085002218d4be59e668bac718abbc6",
            "https://launcher.mojang.com/v1/objects/e80d9b3bf5085002218d4be59e668bac718abbc6/client.jar",
            5256245);
    public static Download server = new Download("952438ac4e01b4d115c5fc38f891710c4941df29",
            "https://launcher.mojang.com/v1/objects/952438ac4e01b4d115c5fc38f891710c4941df29/server.jar",
            9605030);
    public static Download windowsServer = new Download("a79b91ef69b9b4af63d1c7007f60259106869b21",
            "https://launcher.mojang.com/v1/objects/a79b91ef69b9b4af63d1c7007f60259106869b21/windows_server.exe",
            9999270);

    public static HashMap<String, Download> downloads_patch = new HashMap<String, Download>() {{
        put("client", client);
        put("server", server);
        put("windows_server", client);
    }};

            /* backup data
            "LinkedTreeMap downloads\n" +
            "            key = \"client\"\n" +
            "            Download value\n" +
            "                sha1 = \"e80d9b3bf5085002218d4be59e668bac718abbc6\"\n" +
            "                url = \"https://launcher.mojang.com/v1/objects/e80d9b3bf5085002218d4be59e668bac718abbc6/client.jar\"\n" +
            "                size = 5256245\n" +
            "            key = \"server\"\n" +
            "            Download value\n" +
            "                sha1 = \"952438ac4e01b4d115c5fc38f891710c4941df29\"\n" +
            "                url = \"https://launcher.mojang.com/v1/objects/952438ac4e01b4d115c5fc38f891710c4941df29/server.jar\"\n" +
            "                size = 9605030\n" +
            "            key = \"windows_server\"\n" +
            "            Download value\n" +
            "                sha1 = \"a79b91ef69b9b4af63d1c7007f60259106869b21\"\n" +
            "                url = \"https://launcher.mojang.com/v1/objects/a79b91ef69b9b4af63d1c7007f60259106869b21/windows_server.exe\"\n" +
            "                size = 9999270";*/
}
