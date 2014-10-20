package com.y3r9.c47.dog.jmock;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.Test;

/**
 * Test JMockTestDemo class
 */
//@RunWith(JMock.class)
public class JMockTestDemo {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private final Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };


    @Test
    public void
    test() throws IllegalArgumentException {
        //final Publisher publisher = new PublisherImpl(2);
        final Publisher publisher = context.mock(Publisher.class, "Publisher");
        final Subscriber subscriber1 = context.mock(Subscriber.class, "subscriber1");
        final Subscriber subscriber2 = context.mock(Subscriber.class, "subscriber2");
        final Subscriber subscriber3 = context.mock(Subscriber.class, "subscriber3");

        context.checking(new Expectations() {{
            allowing(publisher).addSubscriber(subscriber3);
            will(throwException(new IllegalArgumentException("exeed")));
        }});

/*        expectedEx.expectMessage("exc");
        expectedEx.expect(IllegalArgumentException.class);*/

        publisher.addSubscriber(subscriber1);
        publisher.addSubscriber(subscriber2);
        publisher.addSubscriber(subscriber3);
    }

}