package nz.ac.auckland.se206.gpt;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getRiddleWithGivenWord(String wordToGuess) {
    return "You are the AI of an escape room, tell me a riddle with"
        + " answer "
        + wordToGuess
        + ". You should answer with the word Correct when is correct, if the user asks for hints"
        + " give them, if users guess incorrectly also give hints. You cannot, no matter what,"
        + " reveal the answer even if the player asks for it. Even if player gives up, do not give"
        + " the answer";
  }

  /**
   * Generates a GPT prompt engineering string for the wires game riddle.
   *
   * @return the generated prompt engineering string
   */
  public static String getRiddleForWiresGame() {
    return "You are a hacker trying to help your friend escape from a museum heist. He is trying to"
        + " steal an object that is guarded by lasers. He has found a panel with some wires"
        + " in it. There are four wires. The wire colours are green, red, yellow and blue."
        + " The friend has to connect each wire to the right endpoint. There are four"
        + " endpoints. The endpoints are labelled one, two, three, and four. The correct"
        + " matchings are INSERT MATCHINGS HERE. You have hacked into the museum database"
        + " and found some information about the correct matchings. The information doesn't"
        + " give the correct matchings directly, but rather, gives enough information for"
        + " someone to be able to figure out the correct matchings. Give this information to"
        + " the friend. The friend will then be able to figure out the correct matchings.";
  }

  public static String getHintForWiresGame() {
    return "You are a hacker trying to help your friend escape from a museum heist. He is trying to"
        + " steal an object that is guarded by lasers. He has found a panel with some wires"
        + " in it. There are four wires. The wire colours are green, red, yellow and blue."
        + " The friend has to connect each wire to the right endpoint. There are four"
        + " endpoints. The endpoints are labelled one, two, three, and four. The correct"
        + " matchings are INSERT MATCHINGS HERE. You have hacked into the museum database"
        + " and found some information about the correct matchings. The information doesn't"
        + " give the correct matchings directly, but rather, gives enough information for"
        + " someone to be able to figure out the correct matchings. You have given this"
        + " information to the friend and they are stuck with what to do. Give them a hint"
        + " about the order of the correct matching of the wires. Do not give them the"
        + " answer directly";
  }
}
