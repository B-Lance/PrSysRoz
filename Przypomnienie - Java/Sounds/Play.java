package com.mindprod.vercheck;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import static java.lang.System.*;
import java.net.URL;

/**
 * Plays a sound using the javax.sound.sampled classes.
 * It plays the sound in the background. If you play another sound, before the
 * first has finished, it won't play the sounds on top of each other. It will block,
 * and not return until the first sound has finished at it has started the second.
 * Created by IntelliJ IDEA.
 * version 1.1 2008-09-01 initial version
 *
 * created with Intellij Idea
 *
 * @author Roedy Green, Canadian Mind Products
 */
class Play implements Runnable
    {
    // ------------------------------ FIELDS ------------------------------

    /**
     * true if want debugging output
     */
    private static final boolean DEBUGGING = false;

    /**
     * information about the format of the sound data.
     */
    private final AudioFormat af;

    /**
     * stream current background thread is playing.
     */
    private final AudioInputStream ais;

    /**
     * information about the output sound rendering device
     */
    private final DataLine.Info info;

    /**
     * size of buffer used to copy sound bytes across
     */
    private final int buffSize;
    // -------------------------- PUBLIC STATIC METHODS --------------------------

    /**
     * play a sound file supported by javax.sound.sampled e.g. a signed PCM 8-bit au resource.
     *
     * @param url usually a resource to play created with Class.getResource.
     *
     * @throws UnsupportedAudioFileException if you select a sound file type not supported on this platform.
     * @throws IOException                   if problem retrieving the URL
     */
    public static void play( final URL url ) throws UnsupportedAudioFileException, IOException
        {
        play( AudioSystem.getAudioInputStream( url ) );
        }

    /**
     * play a sound file supported by javax.sound.sampled e.g. a signed PCM 8-bit au resource.
     *
     * @param file a sound file to play
     *
     * @throws UnsupportedAudioFileException if you select a sound file type not supported on this platform.
     * @throws IOException                   if problem retrieving the file.
     */
    public static void play( final File file ) throws UnsupportedAudioFileException, IOException
        {
        play( AudioSystem.getAudioInputStream( file ) );
        }

    // -------------------------- PUBLIC INSTANCE  METHODS --------------------------
    /**
     * background thread to feed bytes from stream to sound renderer
     */
    public void run()
        {
        synchronized ( getClass() )
            {
            try
                {
                final SourceDataLine line = ( SourceDataLine ) AudioSystem.getLine( info );
                line.open( af, buffSize );
                // start streaming in from the resource
                line.start();
                final byte[] data = new byte[buffSize];
                int bytesRead;
                /* copy from stream to sound renderer */
                while ( ( bytesRead = ais.read( data, 0, data.length ) ) != -1 )
                    {
                    line.write( data, 0, bytesRead );
                    }
                /* sound  data stream is finished, wait for sound to finish */
                line.drain();
                line.stop();
                line.close();
                }
            catch ( LineUnavailableException e )
                {
                // throwing an exception in a background thread probably won't be caught properly.
                err.println( "Line unavailable to play a sound" );
                }
            catch ( IOException e )
                {
                err.println( "Problems fetching data to play a sound" );
                }
            }
        }

    // -------------------------- STATIC METHODS --------------------------

    /**
     * play a sound file supported by javax.sound.sampled e.g. a signed PCM 8-bit au resource.
     *
     * @param ais AudioIunputStream to play
     */
    private static synchronized void play( final AudioInputStream ais )
        {
        // check out stream ahead of time so Exception will be fielded.
        final AudioFormat af = ais.getFormat();
        if ( DEBUGGING )
            {
            System.out.println( "audio format: " + af.toString() );
            }

        final DataLine.Info info = new DataLine.Info( SourceDataLine.class, af );
        if ( !AudioSystem.isLineSupported( info ) )
            {
            throw new IllegalArgumentException( "Cannot play an unsupported audio format" );
            }

        final int frameRate = ( int ) af.getFrameRate();
        final int frameSize = af.getFrameSize();
        final int buffSize = frameRate * frameSize / 10;
        if ( DEBUGGING )
            {
            out.println( "Frame Rate: " + frameRate );
            out.println( "Frame Size: " + frameSize );
            out.println( "Buffer Size: " + buffSize );
            }

        // We can't start another thread until previous one has terminated
        // because this method and run are both synchronised on the class object.
        // Start up a background thread to play the sound.
        new Thread( new Play( ais, af, info, buffSize ) ).start();
        }

    // --------------------------- CONSTRUCTORS ---------------------------

    /**
     * constructor to create a thread run object to play a sound in the background.
     *
     * @param ais      audion input stream to play
     * @param af       audio format of the stream
     * @param info     info about the line that renders the sound
     * @param buffSize size of buffer when copying bytes to rendering device.
     */
    private Play( AudioInputStream ais, AudioFormat af, DataLine.Info info, int buffSize )
        {
        this.ais = ais;
        this.af = af;
        this.info = info;
        this.buffSize = buffSize;
        }
    }