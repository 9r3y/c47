package com.y3r9.c47.dog.algorithms;

import java.util.Arrays;

/**
 * �����㷨����BFPRT
 * ���Բ����㷨
 *
 * @author jiyi
 *
 */
public class BFPRT {

    /**
     *
     * @param a
     * @param id
     * @return
     */
    public static <T extends Comparable<T>> T get(T[] a, int id) {
        return get(a,0,a.length,id);

    }
    /**
     * BFPRT�㷨ʵ�֡��� �ڴ���������У���õ�id����ŵ���ֵ�� ��Ŵ�1��ʼ�� ������a�±�l��r�еĵ�id����
     *
     * @param a
     * @param fromIndex
     * @param toIndex
     * @param id
     * @return
     */
    public static <T extends Comparable<T>> T get(T[] a, int fromIndex, int toIndex, int id) {
        // С�ڵ���5������ֱ������õ����
        if (toIndex - fromIndex <= 5) {
            Arrays.sort(a, fromIndex, toIndex);
            return a[fromIndex + id - 1];
        }

        int t = fromIndex - 1; // ��ǰ�滻��ǰ�����λ�����±�
        for (int st = fromIndex, ed; (ed = st + 5) <= toIndex; st += 5) // ÿ5�����д���
        {
            Arrays.sort(a, st, ed); // 5����������
            t++;
            swap(a, t, st + 2); // ����λ���滻������ǰ�棬���ڵݹ���ȡ��λ������λ��
        }

        int pivotId = (fromIndex + t) >> 1; // l��t����λ�����±꣬��Ϊ��Ԫ���±�
        get(a, fromIndex, t, pivotId - fromIndex + 1);// ��������λ����ֵ����֤��λ������ȷ��λ��
        int m = partition(a, fromIndex, toIndex, pivotId), cur = m - fromIndex + 1;
        if (id == cur)
            return a[m]; // �պ��ǵ�id����
        else if (id < cur)
            return get(a, fromIndex, m, id);// ��id���������
        else
            return get(a, m + 1, toIndex, id - cur); // ��id�������ұ�
    }

    private static void swap(Object[] a, int t, int i) {
        Object tmp = a[t];
        a[t] = a[i];
        a[i] = tmp;
    }

    // ������a�±��l��r��Ԫ�ؽ��л���
    private static <T extends Comparable<T>> int partition(T[] a, int l, int r, int pivotId) {
        // ��pivotId����Ԫ��Ϊ������Ԫ
        swap(a, pivotId, --r);
        int j = l - 1; // ����������ҵ��±�
        for (int i = l; i < r; i++)
            if (a[i].compareTo(a[r]) <= 0)
                swap(a, ++j, i);
        swap(a, ++j, r);
        return j;
    }
}
