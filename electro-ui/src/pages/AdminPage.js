import React, { useContext, useState } from 'react';
import { AuthContext } from '../contexts/AuthContext';

const AdminPage = () => {
    const { user, isEmployee } = useContext(AuthContext);

    const initialState = {
        Products: '',
        Categories: '',
        Orders: '',
    };

    const [selectedAction, setSelectedAction] = useState('');

    const handleActionChange = (item, value) => {
      setSelectedAction(value);
  };

    const handleModify = (item) => {
        // Implement the logic to perform the selected action for the item
        console.log(`Perform ${selectedAction} on ${item}`);
    };

    return (
        user && isEmployee ? (
            <div className="container mx-auto mt-8">
                <div className="text-center text-2xl font-semibold mb-4">Admin Panel</div>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    {Object.keys(initialState).map((item) => (
                        <div key={item} className="bg-white p-6 rounded-md shadow-md">
                            <label className="block text-gray-700 font-bold mb-2">{item}</label>
                            <select
                                value={selectedAction}
                                onChange={(event) => handleActionChange(item, event.target.value)}
                                className="border rounded px-2 py-1 w-full"
                            >
                                <option value="">Select Action</option>
                                <option value="add">Add</option>
                                <option value="update">Update</option>
                                <option value="delete">Delete</option>
                            </select>
                            <button
                                onClick={() => handleModify(item)}
                                disabled={!selectedAction}
                                className="mt-4 bg-red-500 text-white px-4 py-2 rounded disabled:bg-gray-400"
                            >
                                Modify
                            </button>
                        </div>
                    ))}
                </div>
            </div>
        ) : null
    );
};

export default AdminPage;
