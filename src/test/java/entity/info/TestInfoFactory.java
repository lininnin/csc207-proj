package entity.info;

/**
 * Test factory that creates TestInfo instances which allow empty strings and preserve spaces.
 * Used only for testing edge cases in InfoFactoryTest.
 */
class TestInfoFactory implements InfoFactoryInterf {

    @Override
    public InfoInterf create(String name, String description, String category) {
        // Create TestInfo that preserves exact input without validation
        return new TestInfo(name, description, category);
    }
}