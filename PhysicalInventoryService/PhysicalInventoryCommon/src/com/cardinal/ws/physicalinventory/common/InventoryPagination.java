package com.cardinal.ws.physicalinventory.common;

import java.util.List;

import com.cardinal.ws.physicalinventory.common.valueobjects.InvPage;

/**
 * @author rohit.bhat
 *
 */
public class InventoryPagination {

	private List<Object> originalList;
	private int pagesize;
	private static final String INVALID_PAGESIZE = "Pagesize must be a positive integer.";
	
	public InventoryPagination(final List<Object> originalList, final int pagesize)	throws IllegalArgumentException {
		if (pagesize <= 0)
			throw new IllegalArgumentException(INVALID_PAGESIZE);
			this.originalList = originalList;
			this.pagesize = pagesize;
	}
	
	/**
	 * This method will return first InvPage results from total results set and
	 * also sets total InvPage Number, current InvPage and results in InvPage constructor
	 */
	public InvPage getFirstPage() {
		InvPage result = null;
		if (originalList != null && !originalList.isEmpty()) {
			result = new InvPage(1, getTotalPage(), pagesize, getPageListFrom(0));
		}
		return result;
	}

	/**
	 * This method will return last InvPage results from total results set and also
	 * sets total InvPage Number, current InvPage and results in InvPage constructor
	 */
	public InvPage getLastPage() {
		InvPage result = null;
		if (originalList != null && !originalList.isEmpty()) {
			final int totalPage = getTotalPage();
			final int startIndex = (totalPage - 1) * pagesize;
			result = new InvPage(totalPage, totalPage, pagesize,
					getPageListFrom(startIndex));
		}
		return result;
	}

	/**
	 * This method will next InvPage results of Current InvPage results from Total
	 * results set and also sets total InvPage Number, current InvPage and results in
	 * InvPage constructor
	 */
	public InvPage getNextPage(final InvPage currentPage) {
		if (currentPage == null)
			return getFirstPage();
		if (currentPage.isLastPage())
			return currentPage;

		InvPage result = null;
		if (originalList != null) {
			result = new InvPage(currentPage.getPageNum() + 1,
					currentPage.getTotalPage(), pagesize,
					getPageListFrom(currentPage.getPageNum() * pagesize));
		}
		return result;
	}

	/**
	 * This method takes pageNum and return requested results of InvPage Object. If
	 * none of the above conditions match then it will calculate retrieve
	 * requested InvPage results and return InvPage object. The object contains total
	 * pages, current InvPage, InvPage size results set etc..
	 * 
	 * @param pageNum
	 * @return
	 */
	public InvPage getPageDetails(final int pageNum) {
		InvPage result = null;
		final int totalPage = getTotalPage();
		if (pageNum < 1 || pageNum > totalPage)
			return null;
		if (originalList != null && !originalList.isEmpty()) {

			result = new InvPage(pageNum, totalPage, pagesize,
					getPageListFrom((pageNum - 1) * pagesize));

		}

		return result;
	}

	/**
	 * This method take current InvPage number and return previous InvPage results
	 * from total results set.
	 */
	public InvPage getPrevPage(final InvPage currentPage) {
		if (currentPage == null)
			return getFirstPage();
		if (currentPage.isFirstPage())
			return currentPage;

		InvPage result = null;
		if (originalList != null) {
			result = new InvPage(currentPage.getPageNum() - 1,
					currentPage.getTotalPage(), pagesize,
					getPageListFrom((currentPage.getPageNum() - 2) * pagesize));
		}
		return result;
	}

	/**
	 * This method takes Starting Index and calculate endInex with InvPage size and
	 * return subset results from total results
	 * 
	 * @param startIndex
	 * @return
	 */
	private List<Object> getPageListFrom(final int startIndex) {
		final int totalSize = originalList.size();

		int endIndex = startIndex + pagesize;
		if (endIndex > totalSize)
			endIndex = totalSize;

		return originalList.subList(startIndex, endIndex);
	}

	/**
	 * This method will return total numbers pages
	 * 
	 * @return
	 */
	private int getTotalPage() {
		if (originalList == null || originalList.size() <= 0)
			return 0;
		final int totalSize = originalList.size();
		return ((totalSize - 1) / pagesize) + 1;
	}
}

