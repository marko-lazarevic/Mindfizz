import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mindfizz.LeaderboardEntry
import com.example.mindfizz.R

class LeaderboardAdapter(private val board: MutableList<LeaderboardEntry>) :
    RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.textName)
        val textScore: TextView = itemView.findViewById(R.id.textScore)
        val imageIcon : ImageView = itemView.findViewById(R.id.imageIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.leadboard_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = board[position]
        holder.textName.text = entry.name
        holder.textScore.text = entry.score.toString()


        when (position) {
            0 -> holder.imageIcon.setImageResource(R.drawable.gold)
            1 -> holder.imageIcon.setImageResource(R.drawable.silver)
            2 -> holder.imageIcon.setImageResource(R.drawable.bronze)
            else -> holder.imageIcon.setImageDrawable(null)
        }

    }

    override fun getItemCount(): Int {
        return board.size
    }
}
