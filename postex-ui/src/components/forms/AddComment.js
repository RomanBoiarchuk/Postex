import React from "react";
import {ErrorMessage, Field, Form, Formik} from "formik";
import * as Yup from "yup";
import {postService} from "../../services";
import {Button, FormControl, InputGroup} from "react-bootstrap";

export function AddComment({onSubmit, post}) {
    return (
        <>
            <Formik
                initialValues={{
                    text: ''
                }}
                validationSchema={Yup.object().shape({
                    text: Yup.string().required('Text is required')
                })}
                onSubmit={(comment, {setStatus, setSubmitting, resetForm}) => {
                    setStatus();
                    postService.createComment(post.id, comment)
                        .then(updatedPost => {
                            onSubmit(updatedPost.comments);
                            setSubmitting(false);
                            resetForm();
                        }, error => {
                            setSubmitting(false);
                            setStatus(error);
                        });
                }}
                render={({errors, status, touched, isSubmitting}) => (
                    <Form>
                        <InputGroup className="mb-3">
                            <Field
                                as={FormControl}
                                name="text"
                                placeholder="Write a comment..."
                                className={errors.text && touched.text ? ' is-invalid' : ''}
                            />
                            <InputGroup.Append>
                                <Button type="submit" variant="outline-primary" disabled={isSubmitting}>Post
                                </Button>
                            </InputGroup.Append>
                            <ErrorMessage name="text" component="div" className="invalid-feedback"/>
                        </InputGroup>
                    </Form>
                )}
            />
        </>
    );
}
