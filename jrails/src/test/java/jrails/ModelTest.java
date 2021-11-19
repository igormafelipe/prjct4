package jrails;

import jrails.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class ModelTest {

    class Book extends Model {
        @Column
        public String title;
        @Column
        public String author;
        @Column
        public int num_copies;
    }

    private Model model;

    @Before
    public void setUp() throws Exception {
        model = new Model() {
        };
    }

    @Test
    public void id() {
        assertThat(model.id(), notNullValue());
    }

    @Test
    public void testSaveAndFind() {
        Book b = new Book();
        Book c = new Book();
        // b.title = "haha";
        // b.author = "hoho";
        // b.num_copies = 100;
        // b.save();
        c.save();
        b.title = "Igor";
        b.save();
        c.title = "xd";
        c.save();
        System.out.println("BOOK CLASS: " + Book.class);
        Book d = Model.find(Book.class, 2);
        d.title = "dtitlehe";
        d.save();
        Book e = new Book();
        e.title = "Sophia";
        e.save();
        assert (d.id == b.id && d.author == b.author && d.title == b.title);
    }

    @After
    public void tearDown() throws Exception {
    }
}