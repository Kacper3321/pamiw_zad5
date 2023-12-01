import React, {Component} from 'react';
import axios from '../config/axios-config';
import {format} from "date-fns";
import './CheckOutList.css';

class CheckOutList extends Component {
    constructor() {
        super();
        this.state = {
            checkOuts: [],
            books: [],
            selecteBookId: '',
            editingId: null,
            isEditing: null,
            newCheckOut: {
                //bookId: '',
                checkoutDate: '',
                returnDays: '',
                borrowerFullName: '',
                bookTitle: '', // Dodaj pole bookTitle
                editedReturnDays: '', // Dodaj pole editedReturnDays
                editedBorrowerFullName: '', // Dodaj pole editedBorrowerFullName
            },
            isFormVisible: false,
        };
    }

    componentDidMount() {
        // Pobierz listę wypożyczeń z backendu po załadowaniu komponentu
        this.fetchCheckOuts();

        axios
            .get('/books')
            .then((response) => {
                this.setState({ books: response.data });
            })
            .catch((error) => {
                console.error('Błąd podczas pobierania danych:', error);
            });
    }

    fetchCheckOuts() {
        axios
            .get('/checkouts')
            .then((response) => {
                console.log('Odpowiedź z serwera:', response.data);
                this.setState({checkOuts: response.data});
            })
            .catch((error) => {
                console.error('Błąd podczas pobierania danych:', error);
            });
    }

    handleDeleteClick(id) {
        this.setState({isFormVisible: false});
        // Usuń wpis o określonym ID
        axios
            .delete(`/checkouts/${id}`)
            .then(() => {
                // Po usunięciu odśwież listę wypożyczeń
                this.fetchCheckOuts();
            })
            .catch((error) => {
                console.error('Błąd podczas usuwania danych:', error);
            });
    }

    handleEditClick(id) {
        const { checkOuts } = this.state;
        const checkOutToEdit = checkOuts.find(checkOut => checkOut.id === id);
        // Ustaw ID wpisu, który chcesz edytować
        //this.setState({editingId: id,isFormVisible: false});
        if (checkOutToEdit) {
            this.setState({
                editingId: id,
                isEditing: id,
                //isFormVisible: false,
                newCheckOut: {
                    ...checkOutToEdit, // Skopiuj istniejące dane
                    editedReturnDays: checkOutToEdit.returnDays, // Ustaw wartość początkową
                    editedBorrowerFullName: checkOutToEdit.borrowerFullName, // Ustaw wartość początkową
                },
            });
        }
    }

    handleSaveClick(checkOut) {
        const { editedReturnDays, editedBorrowerFullName, ...rest } = this.state.newCheckOut;
        const updatedCheckOut = { ...checkOut, returnDays: editedReturnDays, borrowerFullName: editedBorrowerFullName, ...rest };
        // Zaktualizuj wpis o określonym ID
       /* axios
            .put(`/checkouts/${checkOut.id}`, checkOut)
            .then(() => {
                // Po zaktualizowaniu odśwież listę wypożyczeń
                this.fetchCheckOuts();
                // Zakończ edycję
                this.setState({editingId: null});
            })
            .catch((error) => {
                console.error('Błąd podczas zapisywania danych:', error);
            });*/
        axios
            .put(`/checkouts/${updatedCheckOut.id}`, updatedCheckOut)
            .then(() => {
                this.fetchCheckOuts();
                this.setState({ editingId: null });
            })
            .catch((error) => {
                console.error('Błąd podczas zapisywania danych:', error);
            });
    }

    handleCancelClick() {
        // Anuluj edycję
        this.setState({editingId: null,isFormVisible: false});
    }


    handleInputChange(event, field) {
        // Obsługa zmiany wartości w formularzu
        /*const {newCheckOut} = this.state;
        newCheckOut[field] = event.target.value;
        //this.setState({newCheckOut});
        if (field === 'returnDays') {
            this.setState({
                newCheckOut: {
                    ...newCheckOut,
                    editedReturnDays: event.target.value, // Aktualizuj editedReturnDays
                },
            });
        } else if (field === 'borrowerFullName') {
            this.setState({
                newCheckOut: {
                    ...newCheckOut,
                    editedBorrowerFullName: event.target.value, // Aktualizuj editedBorrowerFullName
                },
            });
        } else {
            this.setState({ newCheckOut });
        }*/
        const { newCheckOut } = this.state;
        if (field === 'bookId') {
            this.setState({
                newCheckOut: {
                    ...newCheckOut,
                    [field]: event.target.value,
                },
                selectedBookId: event.target.value, // Ustaw selectedBookId
            });
        } else {
            this.setState({
                newCheckOut: {
                    ...newCheckOut,
                    [field]: event.target.value,
                },
            });
        }
    }

    handleAddClick() {
        // Dodaj nowy wpis
        axios
            .post('/checkouts', this.state.newCheckOut)
            .then(() => {
                // Po dodaniu odśwież listę wypożyczeń
                this.fetchCheckOuts();
                // Zresetuj formularz
                this.setState({
                    newCheckOut: {
                        bookId: '',
                        returnDays: '',
                        borrowerFullName: '',
                    },
                    isFormVisible: false,
                });
            })
            .catch((error) => {
                console.error('Błąd podczas dodawania danych:', error);
            });
    }

    render() {
        const {checkOuts, editingId, newCheckOut, isFormVisible, books } = this.state;

        return (
            <div>
                <h1><center>Lista Wypożyczeń</center></h1>
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tytuł Książki</th>
                        <th>Data Wypożyczenia</th>
                        <th>Dni Zwrotu</th>
                        <th>Imię i Nazwisko Wypożyczającego</th>
                        <th>Akcje</th>
                    </tr>
                    </thead>
                    <tbody>
                    {checkOuts.map((checkOut) => (
                        <tr key={checkOut.id}>
                            <td>{checkOut.id}</td>
                            {/*<td>{checkOut.book.title}</td>
                            <td>{format(new Date(checkOut.checkoutDate), "dd.MM.yyyy")}</td>
                            <td>{checkOut.returnDays}</td>
                            <td>{checkOut.borrowerFullName}</td>
                            <td>
                                {editingId === checkOut.id ? (
                                    <>
                                        <button
                                            onClick={() => this.handleSaveClick(checkOut)}
                                            className="save-button"
                                        >
                                            Zapisz
                                        </button>
                                        <button
                                            onClick={() => this.handleCancelClick()}
                                        >
                                            Anuluj
                                        </button>
                                    </>
                                ) : (
                                    <>
                                        <button
                                            onClick={() => this.handleEditClick(checkOut.id)}
                                            className="edit-button"
                                        >
                                            Edytuj
                                        </button>
                                        <button
                                            onClick={() => this.handleDeleteClick(checkOut.id)}
                                            className="delete-button"
                                        >
                                            Usuń
                                        </button>
                                    </>
                                )}
                            </td>*/}
                            {editingId === checkOut.id ? (
                                <>
                                    <td>
                                        <input
                                            type="text"
                                            value={newCheckOut.book.title}
                                            onChange={(e) => this.handleInputChange(e, 'bookTitle')}
                                        />
                                    </td>
                                    <td>
                                        <td>{format(new Date(checkOut.checkoutDate), "dd.MM.yyyy")}</td>
                                    </td>
                                    <td>
                                        {/* Dodaj inputy do edycji innych pól */}
                                        <input
                                            type="text"
                                            value={newCheckOut.returnDays}
                                            onChange={(e) => this.handleInputChange(e, 'returnDays')}
                                        />
                                    </td>
                                    <td>
                                        {/* Dodaj inputy do edycji innych pól */}
                                        <input
                                            type="text"
                                            value={newCheckOut.borrowerFullName}
                                            onChange={(e) => this.handleInputChange(e, 'borrowerFullName')}
                                        />
                                    </td>
                                </>
                            ) : (
                                <>
                                    <td>{checkOut.book.title}</td>
                                    <td>{format(new Date(checkOut.checkoutDate), "dd.MM.yyyy")}</td>
                                    <td>{checkOut.returnDays}</td>
                                    <td>{checkOut.borrowerFullName}</td>
                                </>
                            )}
                            <td>
                                {editingId === checkOut.id ? (
                                    <>
                                        <button
                                            onClick={() => this.handleSaveClick(checkOut)}
                                            className="save-button"
                                        >
                                            Zapisz
                                        </button>
                                        <button
                                            onClick={() => this.handleCancelClick()}
                                        >
                                            Anuluj
                                        </button>
                                    </>
                                ) : (
                                    <>
                                        <button
                                            onClick={() => this.handleEditClick(checkOut.id)}
                                            className="edit-button"
                                        >
                                            Edytuj
                                        </button>
                                        <button
                                            onClick={() => this.handleDeleteClick(checkOut.id)}
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
                <button onClick={() => this.setState({isFormVisible: true})}>
                    Dodaj nowy wpis
                </button>

                {isFormVisible && (
                    <div>
                        <h2>Dodaj Nowy Wpis</h2>
                        <div>
                            <label>Tytuł Książki:</label>
                            <select
                                value={newCheckOut.bookId}
                                onChange={(e) => this.handleInputChange(e, 'bookId')}
                            >
                                <option value="">Wybierz książkę</option>
                                {books.map((book) => (
                                    <option key={book.id} value={book.id}>
                                        {book.title}
                                    </option>
                                ))}
                            </select>

                            <label>Imię i Nazwisko Wypożyczającego:</label>
                            <input
                                type="text"
                                value={newCheckOut.borrowerFullName}
                                onChange={(e) => this.handleInputChange(e, 'borrowerFullName')}
                            />

                            <label>Dni Zwrotu:</label>
                            <input
                                type="text"
                                value={newCheckOut.returnDays}
                                onChange={(e) => this.handleInputChange(e, 'returnDays')}
                            />
                        </div>
                        <button onClick={() => this.handleAddClick()}>Dodaj</button>
                        <button onClick={() => this.handleCancelClick()}>Anuluj</button>
                    </div>
                )}
            </div>
        );
    }
}

export default CheckOutList;

