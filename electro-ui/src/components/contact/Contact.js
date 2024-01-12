import { Link } from 'react-router-dom';
import { FaEnvelope } from 'react-icons/fa';

function Contact() {
    return (
    <div>
      <Link to="/contact">
        <div className="ml-4 flex relative 
                        rounded-full border w-12 h-12 
                        justify-center items-center 
                        bg-white group hover:bg-gray-300">
            <FaEnvelope className="text-3xl" />
        </div>
      </Link>
    </div>
  );
}

export default Contact;