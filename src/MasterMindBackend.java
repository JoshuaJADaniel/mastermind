// Name: Joshua Daniel
// Date: 11/20/2019

// Imports
import java.awt.*;

public class MasterMindBackend {
  // Colors used in program
  public Color[] colorOptions;
  public String[] stringOptions;
  private Color[] colorSecret;
  private String[] stringSecret;

  // Constructor
  public MasterMindBackend() {
    // Set up color/string options
    colorOptions = new Color[]{
      Color.RED,
      Color.BLUE,
      Color.GREEN,
      Color.YELLOW,
      Color.PINK,
      Color.CYAN
    };

    stringOptions = new String[]{
      "RED",
      "BLUE",
      "GREEN",
      "YELLOW",
      "PINK",
      "CYAN"
    };

    // Generate color secret
    generateSecretColors();
  }

  // Generates random colors
  private void generateSecretColors() {
    // Initialize color/string secret array
    colorSecret = new Color[4];
    stringSecret = new String[4];

    // Loop to generate colors to fill Color[] colorSecret
    int cnt = 0;
    while (cnt < colorSecret.length) {
      // Generate random index to access colorOptions
      int rnd = (int) (Math.random() * (colorOptions.length - 1));

      // Denotes whether to continue
      boolean nextIteration = true;

      // Loop over set colors
      for (int i = 0; i < cnt; ++i) {
        // If color has been seen before, break
        if (colorOptions[rnd] == colorSecret[i]) {
          nextIteration = false;
          break;
        }
      }

      // Go to next iteration
      if (nextIteration) {
        colorSecret[cnt] = colorOptions[rnd];
        stringSecret[cnt] = stringOptions[rnd];
        ++cnt;
      }
    }
  }

  // Returns string denoting colors of secret
  public String secretToString() {
    // Start with empty string
    String out = "";

    // Iterate over strings and concatenate
    for (int i = 0; i < stringSecret.length; ++i) {
      if (i != stringSecret.length - 1) {
        out += stringSecret[i] + ", ";
      } else {
        out += stringSecret[i];
      }
    }

    // Return
    return out;
  }

  // Returns int denoting how many colors were correct
  public int determineColorsCorrect(Color[] inputColors) {
    // Start at zero
    int colorCorrect = 0;

    // Loop over all secret colors
    for (int i = 0; i < colorSecret.length; ++i) {
      // Loop over all inputColors
      for (int j = 0; j < inputColors.length; ++j) {
        // Increase correct colors
        if (inputColors[j] == colorSecret[i]) {
          ++colorCorrect;
          break;
        }
      }
    }

    // Return
    return colorCorrect;
  }

  // Returns int denoting how many colors are in right location
  public int determineLocationsCorrect(Color[] inputColors) {
    // Start at zero
    int posCorrect = 0;

    // Loop over all colors
    for (int i = 0; i < colorSecret.length; ++i) {
      // Increase correct locations
      if (inputColors[i] == colorSecret[i]) {
        ++posCorrect;
      }
    }

    // Return
    return posCorrect;
  }
}
