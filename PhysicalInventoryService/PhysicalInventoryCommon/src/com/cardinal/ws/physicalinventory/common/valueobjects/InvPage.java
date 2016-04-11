package com.cardinal.ws.physicalinventory.common.valueobjects;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.cardinal.ws.physicalinventory.common.InventoryConstants;
/**
 * @author rohit.bhat
 *
 */
public class InvPage implements Serializable {

private static final long serialVersionUID = -3972459361448929642L;
	
	private int PageNum; // NOPMD by stephen.perry01 on 5/25/13 1:06 PM  Don't want to break schema
	private int totalPage;
	private int Pagesize; // NOPMD by stephen.perry01 on 5/25/13 1:06 PM  Don't want to break schema
	private List contentList;
	private String prevLink;
	private String nextLink;

	  public InvPage (final int PageNum,
	      final int totalPage,final int Pagesize,
	      final List contentList) {
	    this.PageNum = PageNum;
	    this.totalPage = totalPage;
	    this.Pagesize = Pagesize;
	    this.contentList = contentList;
	    this.prevLink= (this.PageNum>1? String.valueOf(PageNum-1):"");
	    this.nextLink= (this.PageNum<totalPage?String.valueOf(PageNum+1):"");
	    
	  }

	  public int getPageNum() {
	    return PageNum;
	  }

	  public int getTotalPage() {
	    return totalPage;
	  }

	  public int getPagesize() {
	    return Pagesize;
	  }

	  public List getContentList() {
	    return contentList;
	  }

	  public boolean isFirstPage () {
	    return PageNum == 1;
	  }

	  public boolean isLastPage () {
	    return PageNum == totalPage;
	  }

	  public String getPrevLink() {
		return prevLink;
	}

	public String getNextLink() {
		return nextLink;
	}

	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (!(o instanceof InvPage)) return false;

	    final InvPage InvPage = (InvPage) o;

	    if (PageNum != InvPage.PageNum) return false;
	    if (Pagesize != InvPage.Pagesize) return false;
	    if (totalPage != InvPage.totalPage) return false;
	    if (contentList != null ? 
	      !isListEqual (contentList, InvPage.contentList) 
	      : InvPage.contentList != null)
	        return false;

	    return true;
	  }

	  public int hashCode() {
	    int result;
	    result = PageNum;
	    result = 29 * result + totalPage;
	    result = 29 * result + Pagesize;
	    result = 29 * result + (contentList != null ? 
	      listHashCode (contentList) : 0);
	    return result;
	  }

	  private boolean isListEqual (
	    final List a, final List b) {
	    if (a == b || a.equals(b)) return true; // NOPMD by stephen.perry01 on 5/24/13 5:03 PM

	    final Iterator ia = a.iterator ();
	    final Iterator ib = b.iterator ();
	    while (ia.hasNext() && ib.hasNext()) {
	      final Object oa = ia.next();
	      final Object ob = ib.next();
	      if (!oa.equals(ob)) {
	        return false;
	      }
	    }
	    if (ia.hasNext() || ib.hasNext()) {
	      return false;
	    }
	    return true;
	  }

	  private int listHashCode (final List a) {
	    int result = 0;
	    for (Iterator iterator = a.iterator(); 
	      iterator.hasNext();) {
	      final Object o = iterator.next();
	      result = 29 * result + o.hashCode();
	    }
	    return result;
	    }

	  public String toString () {
	    final StringBuffer sb = new StringBuffer ();
	    sb.append ("InvPage ").append (PageNum)
	      .append (" of ").append (totalPage);
	    sb.append (InventoryConstants.NEW_LINE);

	    for (Iterator it = contentList.iterator(); 
	      it.hasNext();) {
	      final Object o = it.next();
	      sb.append (o).append (InventoryConstants.NEW_LINE);
	    }
	    return sb.toString ();
	  }
}
