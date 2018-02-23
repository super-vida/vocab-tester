package cz.prague.vida.vocab.persist;

/**
 * The Class GuiGenerator.
 */
public class GuiGenerator {
	
	/**
	 * Generate id.
	 *
	 * @return the long
	 */
	public static final long generateId(){
		return (System.currentTimeMillis()  & 0xFFFFFFFF);
	}

}
