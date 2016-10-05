package com.gagauz.tracker.web.config;

public class TestLindonDecomp {
	public static void main(String[] args) {
		String str = "abaababaabaab";
		char[] s = str.toCharArray();
		int n = s.length;
		int i = 0;
		while (i < n) {
			int j = i + 1, k = i;
			while (j < n && s[k] <= s[j]) {
				if (s[k] < s[j])
					k = i;
				else
					++k;
				++j;
			}
			while (i <= k) {
				System.out.print(str.substring(i, i + j - k));
				System.out.print(' ');
				i += j - k;
			}
		}
	}
}
