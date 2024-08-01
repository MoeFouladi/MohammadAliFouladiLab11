package mohammadali.fouladi.n01547173.mf;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Moe extends Fragment {

    Button buttonAddCourse;
    Button buttonDelete;
    EditText editTextCourseName, editTextCourseDescription;
    RecyclerView recyclerViewCourses;
    CourseAdapter courseAdapter;
    List<Course> courseList;
    DatabaseReference databaseCourses;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moe, container, false);

        databaseCourses = FirebaseDatabase.getInstance().getReference("courses");
        buttonDelete = view.findViewById(R.id.MoeDeleteCourse);

        buttonAddCourse = view.findViewById(R.id.MoeaddCourse);
        editTextCourseName = view.findViewById(R.id.MoecourseName);
        editTextCourseDescription = view.findViewById(R.id.MoecourseDescription);
        recyclerViewCourses = view.findViewById(R.id.MoeRVCourses);

        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(getContext()));
        courseList = new ArrayList<>();
        courseAdapter = new CourseAdapter((ArrayList<Course>) courseList);
        recyclerViewCourses.setAdapter(courseAdapter);
        editTextCourseName.setFilters(new InputFilter[] { new InputFilter.AllCaps() });

        buttonAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCourse();
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllCourses();
            }
        });

        databaseCourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Course course = postSnapshot.getValue(Course.class);
                    courseList.add(course);
                }
                courseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        return view;
    }

    private void addCourse() {
        String name = editTextCourseName.getText().toString().trim();
        String description = editTextCourseDescription.getText().toString().trim();
        String namePattern = "^[A-Z]{4}-\\d{3,4}$";


        if (name.isEmpty()) {
            editTextCourseName.setError(getString(R.string.course_name_is_required));
            return;
        } else if (!Pattern.matches(namePattern, name)) {
            editTextCourseName.setError(getString(R.string.InvalidName));
            return;
        } else {
            editTextCourseName.setError(null);
        }

        if (description.isEmpty()) {
            editTextCourseDescription.setError(getString(R.string.course_description_is_required));
            return;
        } else {
            editTextCourseDescription.setError(null);
        }

        String id = databaseCourses.push().getKey();
        Course course = new Course(name, description);
        databaseCourses.child(id).setValue(course);
        Toast.makeText(getContext(), R.string.course_added, Toast.LENGTH_SHORT).show();
        editTextCourseName.setText("");
        editTextCourseDescription.setText("");
    }
    private void deleteAllCourses() {
        if (courseList.isEmpty()) {
            Toast.makeText(getContext(), R.string.no_courses_to_delete, Toast.LENGTH_SHORT).show();
        } else {
            databaseCourses.removeValue();
            Toast.makeText(getContext(), R.string.all_courses_deleted, Toast.LENGTH_SHORT).show();
        }
    }
}
