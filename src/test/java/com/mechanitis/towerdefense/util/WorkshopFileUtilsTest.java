package com.mechanitis.towerdefense.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class WorkshopFileUtilsTest {

    private static final String DIRECTORY_THAT_DOES_NOT_EXIST = "myNonsenseDirectory";
    private static final String DIRECTORY_THAT_DOES_EXIST = "myTotallyAwesomeDirectory";

    @Before
    public void setup() throws IOException {
        resetFileSystem();
        Files.createDirectories(Paths.get(DIRECTORY_THAT_DOES_EXIST));
    }

    @Test
    public void shouldCreateTheDirectoryIfItDoesNotExist() throws Exception {
        //given
        final Path directoryThatDoesNotExist = Paths.get(DIRECTORY_THAT_DOES_NOT_EXIST);
        assertThat(Files.exists(directoryThatDoesNotExist), is(false));

        //when
        WorkshopFileUtils.ensureDirectoryExists(directoryThatDoesNotExist);

        //then
        assertThat(Files.exists(directoryThatDoesNotExist), is(true));
    }

    @Test
    public void shouldNotErrorIfTheDirectoryDoesExist() throws Exception {
        //given
        final Path existingDirectory = Paths.get(DIRECTORY_THAT_DOES_EXIST);
        assertThat(Files.exists(existingDirectory), is(true));

        //when
        WorkshopFileUtils.ensureDirectoryExists(existingDirectory);

        //then
        assertThat(Files.exists(existingDirectory), is(true));
    }

    @After
    public void tearDown() throws IOException {
        resetFileSystem();
    }

    private void resetFileSystem() throws IOException {
        Files.deleteIfExists(Paths.get(DIRECTORY_THAT_DOES_NOT_EXIST));
        Files.deleteIfExists(Paths.get(DIRECTORY_THAT_DOES_EXIST));
    }
}
