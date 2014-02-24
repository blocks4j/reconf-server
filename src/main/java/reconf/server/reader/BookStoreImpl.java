package reconf.server.reader;

import java.util.Collection;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.jboss.resteasy.links.AddLinks;
import org.jboss.resteasy.links.LinkResource;

import reconf.server.domain.Book;

public class BookStoreImpl implements BookStore{

	@Override
	@AddLinks
	@LinkResource(Book.class)
	@GET
	@Path("books")
	public Collection<Book> getBooks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@LinkResource
	@POST
	@Path("books")
	public void addBook(Book book) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@AddLinks
	@LinkResource
	@GET
	@Path("book/{id}")
	public Book getBook(@PathParam("id") String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@LinkResource
	@PUT
	@Path("book/{id}")
	public void updateBook(@PathParam("id") String id, Book book) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@LinkResource(Book.class)
	@DELETE
	@Path("book/{id}")
	public void deleteBook(@PathParam("id") String id) {
		// TODO Auto-generated method stub
		
	}

}
