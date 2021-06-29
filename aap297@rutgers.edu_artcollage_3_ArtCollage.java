
/*************************************************************************
*  Compilation:  javac ArtCollage.java
*  Execution:    java ArtCollage Flo2.jpeg
*
*  @author:
*
*************************************************************************/

import java.awt.Color;

public class ArtCollage {

    // The orginal picture
    private Picture original;

    // The collage picture
    private Picture collage;

    // The collage Picture consists of collageDimension X collageDimension tiles
    private int collageDimension;

    // A tile consists of tileDimension X tileDimension pixels
    private int tileDimension;

    /*
     * One-argument Constructor 1. set default values of collageDimension to 4 and
     * tileDimension to 100 2. initializes original with the filename image 3.
     * initializes collage as a Picture of tileDimension*collageDimension x
     * tileDimension*collageDimension, where each pixel is black (see all
     * constructors for the Picture class). 4. update collage to be a scaled version
     * of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage(String filename) {
        this.tileDimension = 100;
        this.collageDimension = 4;

        this.original = new Picture(filename);
        this.collage = new Picture(400, 400);

        for (int i = 0; i < 400; i++) {
            for (int j = 0; j < 400; j++) {
                int agniCol = i * original.width() / 400;
                int agniRow = j * original.height() / 400;
                Color simple = original.get(agniCol, agniRow);
                collage.set(i, j, simple);
            }
        }
    }

    /*
     * Three-arguments Constructor 1. set default values of collageDimension to cd
     * and tileDimension to td 2. initializes original with the filename image 3.
     * initializes collage as a Picture of tileDimension*collageDimension x
     * tileDimension*collageDimension, where each pixel is black (see all
     * constructors for the Picture class). 4. update collage to be a scaled version
     * of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage(String filename, int td, int cd) {
        this.collageDimension = cd;
        this.tileDimension = td;
        this.original = new Picture(filename);

        this.collage = new Picture(td * cd, td * cd);

        for (int i = 0; i < td * cd; i++) {
            for (int j = 0; j < td * cd; j++) {
                int agniCol = i * original.width() / (td * cd);
                int agniRow = j * original.height() / (td * cd);
                Color simple = original.get(agniCol, agniRow);
                collage.set(i, j, simple);
            }
        }

    }

    /*
     * Returns the collageDimension instance variable
     *
     * @return collageDimension
     */
    public int getCollageDimension() {
        return collageDimension;

    }

    /*
     * Returns the tileDimension instance variable
     *
     * @return tileDimension
     */
    public int getTileDimension() {
        return tileDimension;

    }

    /*
     * Returns original instance variable
     *
     * @return original
     */
    public Picture getOriginalPicture() {
        return original;

    }

    /*
     * Returns collage instance variable
     *
     * @return collage
     */
    public Picture getCollagePicture() {
        return collage;

    }

    /*
     * Display the original image Assumes that original has been initialized
     */
    public void showOriginalPicture() {
        original.show();

    }

    /*
     * Display the collage image Assumes that collage has been initialized
     */
    public void showCollagePicture() {
        collage.show();

    }

    /*
     * Replaces the tile at collageCol,collageRow with the image from filename Tile
     * (0,0) is the upper leftmost tile
     *
     * @param filename image to replace tile
     * 
     * @param collageCol tile column
     * 
     * @param collageRow tile row
     */
    public void replaceTile(String filename, int collageCol, int collageRow) {
        Picture picture = new Picture(filename);

        for (int i = 0; i < tileDimension; i++) {
            for (int j = 0; j < tileDimension; j++) {

                int c = i * picture.width() / tileDimension;
                int r = j * picture.height() / tileDimension;
                collage.set(collageCol * tileDimension + i, collageRow * tileDimension + j, picture.get(c, r));
            }
        }
    }

    /*
     * Makes a collage of tiles from original Picture original will have
     * collageDimension x collageDimension tiles, each tile has tileDimension X
     * tileDimension pixels
     */
    public void makeCollage() {
        for (int collageCol = 0; collageCol < collageDimension; collageCol++) {
            for (int collageRow = 0; collageRow < collageDimension; collageRow++) {

                for (int i = 0; i < tileDimension; i++) {
                    for (int j = 0; j < tileDimension; j++) {

                        int c = i * original.width() / tileDimension;
                        int r = j * original.height() / tileDimension;
                        collage.set(collageCol * tileDimension + i, collageRow * tileDimension + j, original.get(c, r));
                    }
                }
            }
        }
    }

    /*
     * Colorizes the tile at (collageCol, collageRow) with component (see CS111 Week
     * 9 slides, the code for color separation is at the book's website)
     *
     * @param component is either red, blue or green
     * 
     * @param collageCol tile column
     * 
     * @param collageRow tile row
     */
    public void colorizeTile(String component, int collageCol, int collageRow) {
        for (int i = 0; i < tileDimension; i++) {
            for (int j = 0; j < tileDimension; j++) {

                Color col = collage.get(collageCol * tileDimension + i, collageRow * tileDimension + j);
                int r = col.getRed();
                int g = col.getGreen();
                int b = col.getBlue();
                if (component == "red") {
                    g = 0;
                    b = 0;
                } else if (component == "green") {
                    r = 0;
                    b = 0;
                } else if (component == "blue") {
                    r = 0;
                    g = 0;
                }

                collage.set(collageCol * tileDimension + i, collageRow * tileDimension + j, new Color(r, g, b));

            }
        }
    }

    /*
     * Grayscale tile at (collageCol, collageRow) (see CS111 Week 9 slides, the code
     * for luminance is at the book's website)
     *
     * @param collageCol tile column
     * 
     * @param collageRow tile row
     */

    public void grayscaleTile(int collageCol, int collageRow) {

        for (int c = collageCol * tileDimension; c < collageCol * tileDimension + tileDimension; c++) {
            for (int r = collageRow * tileDimension; r < collageRow * tileDimension + tileDimension; r++) {
                Color co = collage.get(c, r);
                Color grayscale = Luminance.toGray(co);
                collage.set(c, r, grayscale);
            }
        }
    }

    /*
     *
     * Test client: use the examples given on the assignment description to test
     * your ArtCollage
     */
    public static void main(String[] args) {

        ArtCollage art = new ArtCollage(args[0]);
        art.showCollagePicture();
    }
}
