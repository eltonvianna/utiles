/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.esv.utile.logging.Logger;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 29/09/2017
 */
public final class ResourceUtils {
    
    private static final Logger LOGGER = Logger.getLogger(ResourceUtils.class);
    
    private static final String fileSeparator = "\\" + File.separator;
    private static final Pattern pattern = Pattern.compile(".*[A-Za-z]+.class$");
    
    /**
     * Suppressing default constructor for non instantiability
     */
    private ResourceUtils() {
        throw new AssertionError("Suppress default constructor for non instantiability");
    }
    
    /**
     * @param resourceName
     * @return
     */
    public static String normalize(final String resourceName) {
        CharSequenceUtils.requireNotBlank(resourceName, "resourceName parameter is empty");
        final String path = Paths.get(resourceName).normalize().toString();
        return path.indexOf("\\") == -1 ? path : path.replaceAll("[/\\\\]+", "/");
    }

    /**
     * @param resourceName
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public static List<Path> list(final String resourceName) throws IOException, URISyntaxException {
        CharSequenceUtils.requireNotBlank(resourceName, "resourceName parameter is empty");
        final long startTime = System.currentTimeMillis();
        try {
            if (resourceName.indexOf(File.pathSeparator) == -1) {
                LOGGER.debug(() -> "Listing regular files using Files.walk(). Resource directory: " + resourceName);
                return Files.walk(ResourceUtils.path(resourceName)).filter(Files::isRegularFile).collect(Collectors.toList());
            } else {
                LOGGER.debug(() -> "Listing regular files using File.listFiles() recursively. Class path: " + resourceName);
                final List<Path> files = new ArrayList<>();
                for (final String path : resourceName.split(File.pathSeparator)) {
                    final File file = new File(path);
                    if (file.isDirectory()) {
                        ResourceUtils.list(files, file);
                    } else {
                        files.add(file.toPath());
                    }
                }
                return files;
            }
        } finally {
            LOGGER.debug(() -> "Spent time: " + (System.currentTimeMillis() - startTime) + " ms");
        }
    }
    
    /**
     * @param files
     * @param directory
     */
    private static void list(final List<Path> files, final File directory) { 
        for (final File file : directory.listFiles()) {
            if (file.isDirectory()) {
                ResourceUtils.list(files, file);
            } else {
                files.add(file.toPath());
            }
        }
    }
    
    /**
     * 
     * @param jarPath
     * @return
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public static List<String> listJar(final String jarPath) throws IOException {
        return new JarFile(jarPath).stream().map(JarEntry::getName).collect(Collectors.toList());
    }
    
    /**
     * @param resourceName
     * @throws IOException
     * @throws URISyntaxException
     */
    public static List<String> listClasses(final String resourceName) throws IOException, URISyntaxException {
        final List<String> classes = new ArrayList<>();
        for (final Path resource : ResourceUtils.list(resourceName)) {
            final String name = resource.toString();
            if (pattern.matcher(name).matches()) {
                int idx = name.indexOf("classes");
                if (idx > -1) {
                    LOGGER.debug(() -> "Found class: " + name);
                    final String className = ResourceUtils.toCanonicalName(name.substring(idx + 8));
                    LOGGER.debug(() -> "Canonical name: " + className);
                    classes.add(className);
                }
            }
        }
        return classes;
    }
    
    /**
     * @param className
     * @return
     */
    private static String toCanonicalName(final String className) {
        if (null == className) {
            return null;
        }
        return className.replaceAll(fileSeparator, ".").replaceAll(".class", "");
    }

    /**
     * 
     * @param resourceName
     * @return
     * @throws URISyntaxException 
     * @throws IOException 
     */
    public static Path path(final String resourceName) throws URISyntaxException, IOException {
        CharSequenceUtils.requireNotBlank(resourceName, "resourceName parameter is empty");
        final URI uri = ResourceUtils.get(resourceName).toURI();
        if ("jar".equals(uri.getScheme())) {
            return FileSystems.newFileSystem(uri, Collections.emptyMap()).getPath(resourceName);
        } else {
            return Paths.get(uri);
        }
    }

    /**
     * @param resourceName
     * @return
     */
    public static URL get(final String resourceName) {
        if (CharSequenceUtils.isBlank(resourceName)) {
            return null;
        }
        final URL url1 = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        LOGGER.debug(() -> "Resource name: " + resourceName + ", url1: " + url1);
        if (null == url1) {
            final URL url2 = ResourceUtils.class.getClassLoader().getResource(resourceName);
            LOGGER.debug(() -> "Resource name: " + resourceName + ", url2: " + url2);
            return url2;
        }
        return url1;
     }

    /**
     * @param resourceName
     * @return
     */
    public static InputStream getAsStream(final String resourceName) {
        if (CharSequenceUtils.isBlank(resourceName)) {
            return null;
        }
        final InputStream is1 = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
        LOGGER.debug(() -> "Resource name: " + resourceName + ", inputStream1: " + is1);
        if (null == is1) {
            final InputStream is2 = ResourceUtils.class.getClassLoader().getResourceAsStream(resourceName);
            LOGGER.debug(() -> "Resource name: " + resourceName + ", inputStream2: " + is2);
            return is2;
        }
        return is1;
     }
    
    /**
     * 
     * @param resourceName
     * @param other
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public static boolean endsWith(final String resourceName, final String other) throws URISyntaxException, IOException  {
        return ResourceUtils.path(resourceName).endsWith(other);
    }
}