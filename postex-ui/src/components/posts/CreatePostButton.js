import {Button, Modal, Spinner} from "react-bootstrap";
import React, {useState} from "react";
import {ErrorMessage, Field, FieldArray, Form, Formik} from "formik";
import * as Yup from 'yup';
import {postService} from "../../services";
import {TagsInput} from "../forms";

export function CreatePostButton() {
    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    let spinner = (
        <Spinner style={{position:'absolute', left:'50%'}} animation="border" role="status">
            <span className="sr-only">Loading...</span>
        </Spinner>
    );

    return (
        <>
            <Button variant="outline-info" size="lg" onClick={handleShow} block>
                Create New Post
            </Button>

            <Modal show={show} size='lg' onHide={handleClose} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Creat a post</Modal.Title>
                </Modal.Header>

                <Formik
                    initialValues={{
                        text: '',
                        tags: []
                    }}
                    validationSchema={Yup.object().shape({
                        text: Yup.string().required('Text is required')
                    })}
                    onSubmit={(post, {setStatus, setSubmitting}) => {
                        setStatus();
                        postService.createPost(post)
                            .then(post => {
                                setSubmitting(false);
                                handleClose();
                            }, error => {
                                setSubmitting(false);
                                setStatus(error);
                            })
                    }}
                    render={({errors, values, status, touched, isSubmitting}) => (
                        <Form>
                            <Modal.Body>
                                <div className="form-group">
                                    <Field style={{width: '100%', height: '200px', maxHeight: '40vh', minHeight: '20vh'}}
                                           name="text" as="textarea"
                                           className={'form-control' + (errors.text && touched.text ? ' is-invalid' : '')}/>
                                    <ErrorMessage name="text" component="div" className="invalid-feedback" style={{display:'block'}}/>
                                </div>
                                <FieldArray name="tags"
                                            render={arrayHelpers =>
                                                <TagsInput arrayHelpers={arrayHelpers} values={values}/>}
                                />
                                {isSubmitting && spinner}
                            </Modal.Body>
                            <Modal.Footer>
                                <Button variant="secondary" onClick={handleClose}>
                                    Close
                                </Button>
                                <Button variant="primary" type="submit" disabled={isSubmitting}>
                                    Create
                                </Button>
                            </Modal.Footer>
                        </Form>
                    )}
                />
            </Modal>
        </>
    );
}

