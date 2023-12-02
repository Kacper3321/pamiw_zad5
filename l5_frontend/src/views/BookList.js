import React, {Component} from 'react';
import axios from '../config/axios-config'; // Zaimportuj odpowiednią bibliotekę do komunikacji z API
import './BookList.css';

class BookList extends Component {
    constructor() {
        super();
        this.state = {
            books: [],
            selectedBookId: '',
            editingId: null,
            isEditing: null,
            currentPage: 1,
            itemsPerPage: 10,
            newBook: {
                title: '',
                author: '',
                year: '',
            },
            isFormVisible: false,
        };
    }

    getCurrentBooks() {
        const {currentPage, itemsPerPage, books} = this.state;
        const indexOfLastItem = currentPage * itemsPerPage;
        const indexOfFirstItem = indexOfLastItem - itemsPerPage;
        return books.slice(indexOfFirstItem, indexOfLastItem);
    }

    componentDidMount() {
        // Pobierz listę książek z backendu po załadowaniu komponentu
        this.fetchBooks();
    }

    fetchBooks() {
        axios
            .get('/books')
            .then((response) => {
                console.log('Odpowiedź z serwera:', response.data);
                const sortedBooks = response.data.sort((a, b) => a.id - b.id);
                this.setState({books: sortedBooks});
            })
            .catch((error) => {
                console.error('Błąd podczas pobierania danych:', error);
            });
    }

    handleDeleteClick(id) {
        this.setState({isFormVisible: false});
        // Usuń książkę o określonym ID
        axios
            .delete(`/books/${id}`)
            .then(() => {
                // Po usunięciu odśwież listę książek
                this.fetchBooks();
            })
            .catch((error) => {
                console.error('Błąd podczas usuwania danych:', error);
            });
    }

    handleEditClick(id) {
        const {books} = this.state;
        const bookToEdit = books.find((book) => book.id === id);
        // Ustaw ID książki, którą chcesz edytować
        if (bookToEdit) {
            this.setState({
                editingId: id,
                isEditing: id,
                isFormVisible: false,
                newBook: {
                    ...bookToEdit, // Skopiuj istniejące dane
                },
            });
        }
    }

    handleSaveClick(book) {
        // Zaktualizuj książkę o określonym ID
        axios
            .put(`/books/${book.id}`, book)
            .then(() => {
                this.fetchBooks();
                this.setState({editingId: null});
            })
            .catch((error) => {
                console.error('Błąd podczas zapisywania danych:', error);
            });
    }

    handleCancelClick() {
        // Anuluj edycję
        this.setState({editingId: null, isFormVisible: false});
    }

    handleInputChange(event, field) {
        // Obsługa zmiany wartości w formularzu
        this.setState({
            newBook: {
                ...this.state.newBook,
                [field]: event.target.value,
            },
        });
    }

    handleAddClick() {
        // Dodaj nową książkę
        axios
            .post('/books', this.state.newBook)
            .then(() => {
                // Po dodaniu odśwież listę książek
                this.fetchBooks();
                // Zresetuj formularz
                this.setState({
                    newBook: {
                        title: '',
                        author: '',
                        year: '',
                    },
                    isFormVisible: false,
                });
            })
            .catch((error) => {
                console.error('Błąd podczas dodawania danych:', error);
            });
    }

    renderPageNumbers() {
        const {currentPage, itemsPerPage, books} = this.state;
        const totalPages = Math.ceil(books.length / itemsPerPage);

        return (
            <div>
                <center>
                    <button
                        disabled={currentPage === 1}
                        onClick={() => this.setState({currentPage: currentPage - 1})}
                    >
                        &laquo; Poprzednia
                    </button>
                    <span> Strona {currentPage} z {totalPages} </span>
                    <button
                        disabled={currentPage === totalPages}
                        onClick={() => this.setState({currentPage: currentPage + 1})}
                    >
                        Następna &raquo;
                    </button>
                </center>
            </div>
        );
    }

    render() {
        const {editingId, newBook, isFormVisible} = this.state;
        const currentBooks = this.getCurrentBooks();

        return (
            <div>
                <h1>
                    <center>Lista Książek</center>
                </h1>
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tytuł</th>
                        <th>Autor</th>
                        <th>Rok</th>
                        <th>Akcje</th>
                    </tr>
                    </thead>
                    <tbody>
                    {currentBooks.map((book) => (
                        <tr key={book.id}>
                            <td>{book.id}</td>
                            <td>
                                {editingId === book.id ? (
                                    <input
                                        type="text"
                                        value={newBook.title}
                                        onChange={(e) => this.handleInputChange(e, 'title')}
                                    />
                                ) : (
                                    book.title
                                )}
                            </td>
                            <td>
                                {editingId === book.id ? (
                                    <input
                                        type="text"
                                        value={newBook.author}
                                        onChange={(e) => this.handleInputChange(e, 'author')}
                                    />
                                ) : (
                                    book.author
                                )}
                            </td>
                            <td>
                                {editingId === book.id ? (
                                    <input
                                        type="text"
                                        value={newBook.year}
                                        onChange={(e) => this.handleInputChange(e, 'year')}
                                    />
                                ) : (
                                    book.year
                                )}
                            </td>
                            <td>
                                {editingId === book.id ? (
                                    <>
                                        <button
                                            onClick={() => this.handleSaveClick(newBook)}
                                            className="save-button"
                                        >
                                            Zapisz
                                        </button>
                                        <button
                                            onClick={() => this.handleCancelClick()}
                                            className="cancel-button"
                                        >
                                            Anuluj
                                        </button>
                                    </>
                                ) : (
                                    <>
                                        <button
                                            onClick={() => this.handleEditClick(book.id)}
                                            className="edit-button"
                                        >
                                            Edytuj
                                        </button>
                                        <button
                                            onClick={() => this.handleDeleteClick(book.id)}
                                            className="delete-button"
                                        >
                                            Usuń
                                        </button>
                                    </>
                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                <button
                    onClick={() =>
                        this.setState({
                            isFormVisible: true,
                            newBook: {
                                title: '',
                                author: '',
                                year: '',
                            },
                        })
                    }
                >
                    Dodaj nową książkę
                </button>
                {isFormVisible && (
                    <div>
                        <h2>Dodaj Nową Książkę</h2>
                        <div>
                            <label>Tytuł:</label>
                            <input
                                type="text"
                                value={newBook.title}
                                onChange={(e) => this.handleInputChange(e, 'title')}
                            />

                            <label>Autor:</label>
                            <input
                                type="text"
                                value={newBook.author}
                                onChange={(e) => this.handleInputChange(e, 'author')}
                            />

                            <label>Rok:</label>
                            <input
                                type="text"
                                value={newBook.year}
                                onChange={(e) => this.handleInputChange(e, 'year')}
                            />
                        </div>
                        <button onClick={() => this.handleAddClick()}>Dodaj</button>
                        <button onClick={() => this.handleCancelClick()} className="cancel-button">
                            Anuluj
                        </button>
                    </div>
                )}
                <div className="pagination">
                    {this.renderPageNumbers()}
                </div>
            </div>
        );
    }
}

export default BookList;
