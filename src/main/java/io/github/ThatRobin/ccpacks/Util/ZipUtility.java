package io.github.ThatRobin.ccpacks.Util;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Screen.LoadingOverlay;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

public class ZipUtility {

    private LoadingOverlay overlay;
    private int total;
    private int current;

    public void zip(List<File> listFiles, String destZipFile, LoadingOverlay overlay, int total) throws
            IOException {
        this.overlay = overlay;
        this.total = total;
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destZipFile));
        for (File file : listFiles) {
            if (file.isDirectory()) {
                zipDirectory(file, file.getName(), zos);
            } else {
                zipFile(file, zos);
            }
        }
        zos.flush();
        zos.close();
    }

    private void zipDirectory(File folder, String parentFolder, ZipOutputStream zos) throws IOException {
        List<File> files = Arrays.stream(folder.listFiles()).toList();
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).isDirectory()) {
                zipDirectory(files.get(i), parentFolder + "/" + files.get(i).getName(), zos);
                continue;
            }
            try {
                zos.putNextEntry(new ZipEntry(parentFolder + "/" + files.get(i).getName()));
                byte[] bytes = Files.readAllBytes(files.get(i).toPath());
                zos.write(bytes, 0, bytes.length);
                current++;
                overlay.setProgress(((float)current/(float)total) * 100);
                zos.closeEntry();
            } catch (ZipException e) {
                CCPacksMain.LOGGER.error(e.getMessage());
            }
        }
    }

    private void zipFile(File file, ZipOutputStream zos) throws IOException {
        try {
            zos.putNextEntry(new ZipEntry(file.getName()));
            byte[] bytes = Files.readAllBytes(file.toPath());
            zos.write(bytes, 0, bytes.length);
            current++;
            overlay.setProgress(((float)current/(float)total) * 100);
            zos.closeEntry();
        } catch (ZipException e) {
            CCPacksMain.LOGGER.error(e.getMessage());
        }
    }
}
