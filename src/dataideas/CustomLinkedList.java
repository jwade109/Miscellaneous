package dataideas;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
// Modified for data ideas package
// Implements CarranoDataStructuresLib’s ListInterface


/**
 * A custom implementation of the List ADT using singly linked nodes. This class
 * also provides an Iterator and is sortable.
 * 
 * @author Adam Wilborn
 * @version 2016.11.12
 * 
 * @author Wade Foster
 * @version 2016.11.12
 * 
 * @author William McDermott
 * @version 2016.11.13
 * 
 * @param <T> generic data type.
 */
public class CustomLinkedList<T> implements Iterable<T>
{
    private Node firstNode;
    private int numberOfEntries;

    /**
     * Default constructor for our CustomLinkedList. 
     * Initializes the class's data fields to indicate an empty list.
     */
    public CustomLinkedList()
    {
        firstNode = null;
        numberOfEntries = 0;
    }

    /**
     * Adds an entry to the end of our CustomLinkedList.
     * @param newEntry The entry to be added to the list.
     */
    public void add(T newEntry)
    {
        if (newEntry == null)
        {
            throw new IllegalArgumentException("Can't add null entry");
        }
        Node newNode = new Node(newEntry);
        if (isEmpty())
        {
            firstNode = newNode;
        }
        else
        {
            Node lastNode = getNodeAt(numberOfEntries - 1);
            lastNode.setNextNode(newNode);
        }
        numberOfEntries++;
    }

    /**
     * Adds an entry to a specified position in our CustomLinkedList.
     * @param newPosition The position in the list where the entry 
     * will be added.
     * @param newEntry The entry to be added to the list.
     */
    public void add(int newPosition, T newEntry)
    {
        if (newEntry == null)
        {
            throw new IllegalArgumentException("Can't add null entry");
        }
        if ((newPosition >= 0) && (newPosition <= numberOfEntries))
        {
            Node newNode = new Node(newEntry);
            if (newPosition == 0)
            {
                newNode.setNextNode(firstNode);
                firstNode = newNode;
            }
            else
            {
                Node nodeBefore = getNodeAt(newPosition - 1);
                Node nodeAfter = nodeBefore.getNextNode();
                newNode.setNextNode(nodeAfter);
                nodeBefore.setNextNode(newNode);
            }
            numberOfEntries++;
        }
        else
        {
            throw new IndexOutOfBoundsException(
                    "Index does not exist: " + newPosition);
        }
    }

    /**
     * Removes all entries in our list.
     */
    public void clear()
    {
        firstNode = null;
        numberOfEntries = 0;
    }

    /**
     * Checks to see if the list contains a specified entry. 
     * @param anEntry The entry to look for in the list.
     * @return True if the list contains the entry and false if not.
     */
    public boolean contains(T anEntry)
    {
        boolean found = false;
        Node currentNode = firstNode;
        while (!found && (currentNode != null))
        {
            if (anEntry.equals(currentNode.getData()))
            {
                found = true;
            }
            else
            {
                currentNode = currentNode.getNextNode();
            }
        }
        return found;
    }

    /**
     * Goes through the list and returns the entry at the specified position.
     * @param givenPosition The position of the entry we want to get.
     * @return The entry at the specified position. If the position is not in
     * the list, it will throw a new IndexOutOfBoundsException.
     */
    public T getEntry(int givenPosition)
    {
        if ((givenPosition >= 0) && (givenPosition <= numberOfEntries - 1))
        {
            return getNodeAt(givenPosition).getData();
        }
        else
        {
            throw new IndexOutOfBoundsException(
                    "Index does not exist: " + givenPosition);
        }
    }

    /**
     * Gets the number of entries in the list.
     * @return number of entries.
     */
    public int getLength()
    {
        return numberOfEntries;
    }

    /**
     * Checks if this list is empty.
     * @return true if the list is empty, and false if not.
     */
    public boolean isEmpty()
    {
        return numberOfEntries == 0;
    }

    /**
     * Removes the entry in the list at the specified position.
     * @param givenPosition The position of the entry that needs to be removed.
     * @return The entry being removed. If the specified position is not in the
     * list, throw a new IndexOutOfBoundsException.
     */
    public T remove(int givenPosition)
    {
        T result = null;
        if ((givenPosition >= 0) && (givenPosition <= numberOfEntries - 1))
        {
            if (givenPosition == 0)
            {
                result = firstNode.getData();
                firstNode = firstNode.getNextNode();
            }
            else
            {
                Node nodeBefore = getNodeAt(givenPosition - 1);
                Node nodeToRemove = nodeBefore.getNextNode();
                result = nodeToRemove.getData();
                Node nodeAfter = nodeToRemove.getNextNode();
                nodeBefore.setNextNode(nodeAfter);
            }
            numberOfEntries--;
            return result;
        }
        else
        {
            throw new IndexOutOfBoundsException(
                    "Index does not exist: " + givenPosition);
        }
    }

    /**
     * Replaces the entry at the specified position with a new specified entry.
     * @param givenPosition The position of the entry that needs to be replaced.
     * @param newEntry The entry that will replace the entry at the given
     *  position.
     * @return The original entry being replaced. If the given position is not
     * in our list, this will throw a new IndexOutOfBounds exception.
     */
    public T replace(int givenPosition, T newEntry)
    {
        if ((givenPosition >= 0) && (givenPosition <= numberOfEntries - 1))
        {
            Node desiredNode = getNodeAt(givenPosition);
            T originalEntry = desiredNode.getData();
            desiredNode.setData(newEntry);
            return originalEntry;
        }
        else
        {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Creates an array of entries based on the entries in the list.
     * @return An array of all the entries from our list.
     */
    public T[] toArray()
    {
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Object[numberOfEntries];
        int index = 0;
        Node currentNode = firstNode;
        while ((index < numberOfEntries))
        {
            result[index] = currentNode.getData();
            currentNode = currentNode.getNextNode();
            index++;
        }
        return result;
    }

    /**
     * Creates a string of all the entries based on the entries in the list.
     * @return A string of all the entries from our list.
     */
    public String toString()
    {
        StringBuilder out = new StringBuilder("[");
        int count = 0;
        for (T entry : this)
        {
            count++;
            out.append(entry.toString());
            if (count < numberOfEntries)
            {
                out.append(", ");
            }
        }
        out.append("]");
        return out.toString();
    }

    /**
     * Returns a reference to the node at a given position.
     * @param givenPosition The position of node we want to return.
     * @return The node at the given position.
     */
    private Node getNodeAt(int givenPosition)
    {
        Node currentNode = firstNode;
        for (int i = 0; i < givenPosition; i++)
        {
            currentNode = currentNode.getNextNode();
        }
        return currentNode;
    }

    /**
     * Private inner class Node with set and get methods.
     * @author Adam Wilborn (adamw97)
     * @version 11/10/2016
     */
    private class Node
    {
        private T data;
        private Node next;

        /**
         * Default constructor that creates a new Node with next equal to null.
         * @param dataPortion The data of the null.
         */
        private Node(T dataPortion)
        {
            this(dataPortion, null);
        }

        /**
         * Constructor that creates a new Node with next equal to a specified
         * nextNode.
         * @param dataPortion The data of the null.
         * @param nextNode The Node that we will set next equal to.
         */
        private Node(T dataPortion, Node nextNode)
        {
            data = dataPortion;
            next = nextNode;
        }

        /**
         * Gets the data of a Node.
         * @return The data of the Node.
         */
        private T getData()
        {
            return data;
        }

        /**
         * Sets the data of a Node.
         * @param newData The data we want to give to a Node.
         */
        private void setData(T newData)
        {
            data = newData;
        }

        /**
         * Gets the next Node.
         * @return The next Node.
         */
        private Node getNextNode()
        {
            return next;
        }

        /**
         * Sets the next Node.
         * @param nextNode The Node we want to be the next Node.
         */
        private void setNextNode(Node nextNode)
        {
            next = nextNode;
        }
    }

    /**
     * Returns a new instance of the class IteratorForLinkedList
     * @return A new IteratorForLinkedList
     */
    public Iterator<T> iterator()
    {
        return new IteratorForLinkedList();
    }

    /**
     * A class that represents the iterator for our list.
     * @author Adam Wilborn (adamw97)
     * @version 11/10/2016
     */
    private class IteratorForLinkedList implements Iterator<T>
    {
        private Node nextNode;

        /**
         * Default constructor that initializes the nextNode field.
         */
        private IteratorForLinkedList()
        {
            nextNode = firstNode;
        }

        /**
         * Checks to see if there if there is a next entry.
         * @return True if there is a next entry and false if not.
         */
        public boolean hasNext()
        {
            return nextNode != null;
        }

        /**
         * Iterates to the next entry.
         * @return The next entry. If there is not next entry this
         * will throw a new NoSuchElementException.
         */
        public T next()
        {
            if (hasNext())
            {
                Node returnNode = nextNode;
                nextNode = nextNode.getNextNode();
                return returnNode.getData();
            }
            else
            {
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * Sorts the list using an Insertion Sort.
     * @param comparator The comparator to base this sort off of.
     */
    public void sort(Comparator<T> comparator)
    {
        // This effectively clears the old list and supplies a new list
        firstNode = sortRecursive(this, comparator).firstNode;
    }

    /**
     * Helper method that recursively perform an insertion sort on a
     * CustomLinkedList.
     * @param list The CustomLinkedList to perform the sort on.
     * @param comparator The comparator that says how to sort the list.
     * @return The CustomLinkedList sorted.
     */
    private CustomLinkedList<T> sortRecursive(CustomLinkedList<T> list,
            Comparator<T> comparator)
    {
        if (list.getLength() < 2)
        {
            return list;
        }
        // Sorting the rest of the list, minus the first element
        T firstElement = firstNode.getData();
        list.remove(0);
        list = sortRecursive(list, comparator);
        Iterator<T> inserter = list.iterator();
        // finding the place of the first element
        int i = 0;
        while (inserter.hasNext()
                && comparator.compare(firstElement, inserter.next()) > 0)
        {
            i++;
        }
        // Adding the first element in the right place
        list.add(i, firstElement);
        return list;
    }
}
