import React from 'react';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { toast } from 'react-hot-toast';
import axios from 'axios';

const ContactForm = () => {
  const formik = useFormik({
    initialValues: {
      name: '',
      email: '',
      subject: '',
      message: '',
    },
    validationSchema: Yup.object({
      name: Yup.string().required('Required'),
      email: Yup.string().email('Invalid email address').required('Required'),
      subject: Yup.string().required('Required'),
      message: Yup.string().required('Required'),
    }),
    onSubmit: async (values, { resetForm }) => {
      try {
        const response = await axios.post('/contact', values);
        if (response) {
          resetForm();
          toast.success('Message sent successfully');
        }
      } catch {
        toast.error('Failed to send message. Please try again later');
      }
    },
  });

  return (
    <div className="max-w-md mx-auto mt-32 p-8 bg-white shadow-md rounded">
      <h2 className="text-4xl text-center font-extrabold text-red-600 mb-6">Let's Connect!</h2>
      <form onSubmit={formik.handleSubmit}>
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="name">
            Name
          </label>
          <input
            type="text"
            id="name"
            name="name"
            className="w-full p-2 border rounded"
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            value={formik.values.name}
            disabled={formik.isSubmitting}
          />
          {formik.touched.name && formik.errors.name ? (
            <div className="text-red-500">{formik.errors.name}</div>
          ) : null}
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="email">
            Email
          </label>
          <input
            type="email"
            id="email"
            name="email"
            className="w-full p-2 border rounded"
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            value={formik.values.email}
            disabled={formik.isSubmitting}
          />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="subject">
            Subject
          </label>
          <input
            type="text"
            id="subject"
            name="subject"
            className="w-full p-2 border rounded"
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            value={formik.values.subject}
            disabled={formik.isSubmitting}
          />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="message">
            Message
          </label>
          <textarea
            id="message"
            name="message"
            rows="4"
            className="w-full p-2 border rounded"
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            value={formik.values.message}
            disabled={formik.isSubmitting}
          ></textarea>
        </div>
        <div className='flex justify-center'>
          <button
            type="submit"
            className="bg-red-500 text-white py-2 px-4 rounded hover:bg-red-700"
            disabled={formik.isSubmitting}
          >
            {formik.isSubmitting  ? 'Submitting...' : 'Submit'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default ContactForm;
