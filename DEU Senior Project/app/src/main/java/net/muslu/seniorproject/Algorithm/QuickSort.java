package net.muslu.seniorproject.Algorithm;

import java.util.List;

// Java program for implementation of QuickSort
class QuickSort
{
    /* This function takes last element as pivot,
       places the pivot element at its correct
       position in sorted array, and places all
       smaller (smaller than pivot) to left of
       pivot and all greater elements to right
       of pivot */
    int partition(List<Chromosome> pop, int low, int high)
    {
        Chromosome pivot = pop.get(high);
        int i = (low-1); // index of smaller element
        for (int j=low; j<high; j++)
        {
            // If current element is smaller than the pivot
            if (pop.get(j).getFitnessScore() > pivot.getFitnessScore())
            {
                i++;
                // swap arr[i] and arr[j]
                Chromosome temp = pop.get(i);
                pop.set(i, pop.get(j));
                pop.set(j, temp);
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        Chromosome temp = pop.get(i+1);
        pop.set(i+1, pop.get(high));
        pop.set(high, temp);

        return i+1;
    }


    /* The main function that implements QuickSort()
      arr[] --> Array to be sorted,
      low  --> Starting index,
      high  --> Ending index */
    void sort(List<Chromosome> pop, int low, int high)
    {
        if (low < high)
        {
            /* pi is partitioning index, arr[pi] is
              now at right place */
            int pi = partition(pop, low, high);

            // Recursively sort elements before
            // partition and after partition
            sort(pop, low, pi-1);
            sort(pop, pi+1, high);
        }
    }

}
/*This code is contributed by Rajat Mishra */