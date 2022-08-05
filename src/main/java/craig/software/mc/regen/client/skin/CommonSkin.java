package craig.software.mc.regen.client.skin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.NativeImage;
import craig.software.mc.regen.Regeneration;
import craig.software.mc.regen.client.screen.SkinPack;
import craig.software.mc.regen.config.RegenConfig;
import craig.software.mc.regen.util.DownloadSkinsThread;
import craig.software.mc.regen.util.MineSkin;
import craig.software.mc.regen.util.PlayerUtil;
import craig.software.mc.regen.util.RegenUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.RandomStringUtils;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CommonSkin {

    public static final File SKIN_DIRECTORY = new File(RegenConfig.COMMON.skinDir.get() + "/regeneration_skins/skins/");
    public static final File SKIN_DIRECTORY_STEVE = new File(SKIN_DIRECTORY, "/steve");
    public static File TRENDING_STEVE = new File(SKIN_DIRECTORY_STEVE + "/mineskin");
    public static final File SKIN_DIRECTORY_ALEX = new File(SKIN_DIRECTORY, "/alex");
    public static File TRENDING_ALEX = new File(SKIN_DIRECTORY_ALEX + "/mineskin");
    public static final File SKIN_DIRECTORY_MALE_TIMELORD = new File(SKIN_DIRECTORY, "/timelord/male");
    public static final File SKIN_DIRECTORY_FEMALE_TIMELORD = new File(SKIN_DIRECTORY, "/timelord/female");

    public static ResourceLocation fileTotexture(File file) {
        NativeImage nativeImage = null;
        try {
            nativeImage = NativeImage.read(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SkinHandler.loadImage(nativeImage);
    }

    //Choose a random PNG from a folder
    public static File chooseRandomSkin(RandomSource rand, boolean isAlex, boolean isTimelord) {
        File skins = isTimelord ? (isAlex ? SKIN_DIRECTORY_FEMALE_TIMELORD : SKIN_DIRECTORY_MALE_TIMELORD) : (isAlex ? SKIN_DIRECTORY_ALEX : SKIN_DIRECTORY_STEVE);

        if (!skins.exists()) {
            try {
                CommonSkin.folderSetup();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        Collection<File> folderFiles = FileUtils.listFiles(skins, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        folderFiles.removeIf(file -> !file.getName().endsWith(".png"));

        if (folderFiles.isEmpty()) {
            DownloadSkinsThread.setup();
            return null;
        }

        return (File) folderFiles.toArray()[rand.nextInt(folderFiles.size())];
    }


    public static void folderSetup() throws IOException {
        File[] folders = new File[]{SKIN_DIRECTORY, SKIN_DIRECTORY_ALEX, SKIN_DIRECTORY_FEMALE_TIMELORD, SKIN_DIRECTORY_MALE_TIMELORD, SKIN_DIRECTORY_STEVE};
        for (File folder : folders) {
            if (!folder.exists()) {
                FileUtils.forceMkdir(folder);
            }
        }
    }

    /**
     * @param url      - URL to download image from
     * @param filename - Filename of the image [SHOULD NOT CONTAIN FILE EXTENSION, PNG IS SUFFIXED FOR YOU]
     */
    public static void downloadSkins(URL url, String filename, File alexDir, File steveDir) throws IOException {
        URLConnection uc = url.openConnection();
        uc.connect();
        uc = url.openConnection();
        uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");
        BufferedImage img = ImageIO.read(uc.getInputStream());
        File file = isAlexSkin(img) ? alexDir : steveDir;
        if (!file.exists()) {
            file.mkdirs();
        }

        if (!steveDir.exists()) {
            steveDir.mkdirs();
        }

        if (!alexDir.exists()) {
            alexDir.mkdirs();
        }

        Regeneration.LOG.info("URL: {} || Name: {} || Path: {}", url.toString(), filename, file.getPath());
        ImageIO.write(img, "png", new File(file, filename + ".png"));
    }

    public static void downloadSkinsSpecific(URL url, String filename, File specific) throws IOException {
        URLConnection uc = url.openConnection();
        uc.connect();
        uc = url.openConnection();
        uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");
        BufferedImage img = ImageIO.read(uc.getInputStream());
        //  img= toBlackAndWhite(img);
        if (!specific.exists()) {
            specific.mkdirs();
        }

        Regeneration.LOG.info("URL: {} || Name: {} || Path: {}", url.toString(), filename, specific.getPath());
        ImageIO.write(img, "png", new File(specific, filename + ".png"));
    }

    public static BufferedImage toBlackAndWhite(BufferedImage img) {
        BufferedImage gray = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        return op.filter(img, gray);
    }



    public static boolean isAlexSkin(BufferedImage image) {
        for (int i = 0; i < 8; i++) {
            if (!hasAlpha(54, i + 20, image) || !hasAlpha(55, i + 20, image)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasAlpha(int x, int y, BufferedImage image) {
        int pixel = image.getRGB(x, y);
        return pixel >> 24 == 0x00 || ((pixel & 0x00FFFFFF) == 0);
    }


    public static void skinpacks() throws IOException {
        if (!RegenConfig.CLIENT.downloadInteralSkins.get() || !RegenUtil.doesHaveInternet()) return;
        String packsUrl = "https://mc-api.craig.software/skins";
        JsonObject links = MineSkin.getApiResponse(new URL(packsUrl));

        for (int skins = links.getAsJsonArray("data").size() - 1; skins >= 0; skins--) {
            JsonArray data = links.getAsJsonArray("data");
            JsonObject currentSkin = data.get(skins).getAsJsonObject();
            String packName = currentSkin.get("name").getAsString();
            String downloadLink = currentSkin.get("url").getAsString();
            String destination = currentSkin.get("destination").getAsString();


            File skinPackDir = new File(SKIN_DIRECTORY + "/" + destination);
            if (skinPackDir.exists()) {
                skinPackDir.mkdirs();
            }
            downloadSkinsSpecific(new URL(downloadLink), packName, skinPackDir);

        }
    }
    public static List<File> listAllSkins(PlayerUtil.SkinType choices) {
        File directory = switch (choices) {
            case EITHER -> SKIN_DIRECTORY;
            case ALEX -> SKIN_DIRECTORY_ALEX;
            case STEVE -> SKIN_DIRECTORY_STEVE;
        };

        if (!directory.exists()) {
            return new ArrayList<>();
        }

        Collection<File> folderFiles = FileUtils.listFiles(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        folderFiles.removeIf(file -> !file.getName().endsWith(".png") || file.getName().contains("timelord_male") || file.getName().contains("timelord_female"));
        return new ArrayList<>(folderFiles);
    }

    public static void trending() throws IOException {
        if (!RegenConfig.CLIENT.downloadTrendingSkins.get() || !RegenUtil.doesHaveInternet()) return;
        File trendingDir = TRENDING_ALEX;
        if (!trendingDir.exists()) {
            if (trendingDir.mkdirs()) {
                Regeneration.LOG.info("Creating Directory: " + trendingDir);
                Regeneration.LOG.info("Creating Directory: " + TRENDING_ALEX);
                Regeneration.LOG.info("Creating Directory: " + TRENDING_STEVE);
            }
        }
        long attr = trendingDir.lastModified();
        if (System.currentTimeMillis() - attr >= 86400000 || Objects.requireNonNull(trendingDir.list()).length == 0) {
            FileUtils.cleanDirectory(trendingDir);
            Regeneration.LOG.warn("Refreshing Trending skins");

            int randomPage = RegenUtil.RAND.nextInt(7800);

            for (int i = 3; i > 0; i--) {
                for (String skin : MineSkin.getSkinsFromPage(randomPage + i)) {
                    downloadSkins(new URL(skin), "mk_" + RandomStringUtils.random(5, true, false), TRENDING_ALEX, TRENDING_STEVE);
                }
            }
        }
    }

    public static void timelord() throws IOException {
        long attr = SKIN_DIRECTORY_MALE_TIMELORD.lastModified();
        if (System.currentTimeMillis() - attr >= 86400000 || Objects.requireNonNull(SKIN_DIRECTORY_MALE_TIMELORD.list()).length == 0) {
            FileUtils.cleanDirectory(SKIN_DIRECTORY_FEMALE_TIMELORD);
            FileUtils.cleanDirectory(SKIN_DIRECTORY_MALE_TIMELORD);
            Regeneration.LOG.warn("Refreshing Timelord skins");

            String[] genders = new String[]{"male", "female"};
            for (String gender : genders) {
                for (String skin : MineSkin.searchSkins(gender)) {
                    downloadSkinsSpecific(new URL(skin), "timelord_" + gender + "_" + RandomStringUtils.random(5, true, false), gender.equals("male") ? SKIN_DIRECTORY_MALE_TIMELORD : SKIN_DIRECTORY_FEMALE_TIMELORD);
                }
            }
        }
    }
}